package semester1.practice10.homework;

import java.util.Locale;

final class ImmutableMatrix2D {
    private final int rows;
    private final int cols;
    private final double[][] data;

    public ImmutableMatrix2D(double[][] source) {
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = source[i][j];
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double get(int r, int c) {
        return data[r][c];
    }

    public double[][] getDataCopy() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = data[i][j];
            }
        }
        return copy;
    }

    public double getTrace() {
        double sum = 0.0;
        for (int i = 0; i < rows && i < cols; i++) {
            sum = sum + data[i][i];
        }
        return sum;
    }

    public double getFrobeniusNorm() {
        double sum = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + data[i][j] * data[i][j];
            }
        }
        return Math.sqrt(sum);
    }

    public double getDeterminant() {
        if (rows == 2 && cols == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        if (rows == 3 && cols == 3) {
            return data[0][0] * data[1][1] * data[2][2]
                    + data[0][1] * data[1][2] * data[2][0]
                    + data[0][2] * data[1][0] * data[2][1]
                    - data[0][2] * data[1][1] * data[2][0]
                    - data[0][0] * data[1][2] * data[2][1]
                    - data[0][1] * data[1][0] * data[2][2];
        }
        return 0.0;
    }

    public double maxAbsDifference(ImmutableMatrix2D other) {
        double max = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double diff = Math.abs(this.data[i][j] - other.data[i][j]);
                if (diff > max) {
                    max = diff;
                }
            }
        }
        return max;
    }
}

interface InvertibleOperator {
    ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed);
}

interface OrthogonalOperator extends InvertibleOperator {

    default boolean verifyNormInvariance(ImmutableMatrix2D before, ImmutableMatrix2D after) {
        return Math.abs(before.getFrobeniusNorm() - after.getFrobeniusNorm()) < 1e-6;
    }
}

abstract class AbstractMatrixOperator {
    private final String operatorName;

    public AbstractMatrixOperator(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getName() {
        return operatorName;
    }

    public abstract boolean canApply(ImmutableMatrix2D m);

    protected abstract ImmutableMatrix2D transform(ImmutableMatrix2D m);

    public final ImmutableMatrix2D execute(ImmutableMatrix2D m) {
        if (m == null || !canApply(m)) {
            return m;
        }
        ImmutableMatrix2D result = transform(m);
        if (this instanceof OrthogonalOperator ort) {
            boolean saved = ort.verifyNormInvariance(m, result);
            System.out.printf(" [Orthogonal: Норма %.4f збережена %s]",
                    result.getFrobeniusNorm(), saved ? "✅" : "❌");
        }
        return result;
    }
}

class NonZeroScaleOperator extends AbstractMatrixOperator implements InvertibleOperator {
    private double factor;

    public NonZeroScaleOperator(double factor) {
        super("Scale(x" + factor + ")");
        this.factor = factor;
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) {
        return factor != 0.0;
    }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        double[][] result = m.getDataCopy();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = result[i][j] * factor;
            }
        }
        return new ImmutableMatrix2D(result);
    }

    @Override
    public ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed) {
        double[][] result = transformed.getDataCopy();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = result[i][j] / factor;
            }
        }
        System.out.println("<- Декодування 'Scale(x" + (1.0 / factor) + ")' виконано...");
        return new ImmutableMatrix2D(result);
    }
}

class TransposeOperator extends AbstractMatrixOperator implements OrthogonalOperator {

    public TransposeOperator() {
        super("Transpose(A^T)");
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) {
        return true;
    }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        double[][] result = new double[m.getCols()][m.getRows()];
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                result[j][i] = m.get(i, j);
            }
        }
        return new ImmutableMatrix2D(result);
    }

    @Override
    public ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed) {
        System.out.println("<- Декодування 'Transpose((A^T)^T)' виконано...");
        return transform(transformed);
    }
}

class GivensRotationOperator extends AbstractMatrixOperator implements OrthogonalOperator {
    private double angleRadians;

    private static double toDegreesRounded(double radians) {
        return Math.round(Math.toDegrees(radians) * 10.0) / 10.0;
    }

    public GivensRotationOperator(double angleRadians) {
        super("GivensRotation(" + toDegreesRounded(angleRadians) + "°)");
        this.angleRadians = angleRadians;
    }

    private ImmutableMatrix2D rotate(ImmutableMatrix2D m, double angle) {
        double[][] result = m.getDataCopy();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (int j = 0; j < m.getCols(); j++) {
            double a = result[0][j];
            double b = result[1][j];
            result[0][j] = cos * a - sin * b;
            result[1][j] = sin * a + cos * b;
        }
        return new ImmutableMatrix2D(result);
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) {
        return m.getRows() >= 2;
    }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        return rotate(m, angleRadians);
    }

    @Override
    public ImmutableMatrix2D applyInverse(ImmutableMatrix2D transformed) {
        System.out.println("<- Декодування 'GivensRotation(" + toDegreesRounded(-angleRadians) + "°)' виконано...");
        return rotate(transformed, -angleRadians);
    }
}

class UpperTriangularProjection extends AbstractMatrixOperator {

    public UpperTriangularProjection() {
        super("UpperTriangularProjection");
    }

    @Override
    public boolean canApply(ImmutableMatrix2D m) {
        return m.getRows() == m.getCols();
    }

    @Override
    protected ImmutableMatrix2D transform(ImmutableMatrix2D m) {
        double[][] result = m.getDataCopy();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < i; j++) {
                result[i][j] = 0.0;
            }
        }
        return new ImmutableMatrix2D(result);
    }
}

public class Task4PolymorphicMatrixSpectralEngine {

    private static void printMatrix(ImmutableMatrix2D m) {
        for (int i = 0; i < m.getRows(); i++) {
            System.out.print("  [");
            for (int j = 0; j < m.getCols(); j++) {
                if (j == 0) {
                    System.out.printf("%9.4f", m.get(i, j));
                } else {
                    System.out.printf("%10.4f", m.get(i, j));
                }
            }
            System.out.println(" ]");
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        ImmutableMatrix2D x0 = new ImmutableMatrix2D(new double[][]{{3.0, 4.0}, {0.0, 5.0}});

        System.out.println("================ СПЕКТРАЛЬНИЙ КОНВЕЄР МАТРИЧНИХ ОПЕРАТОРІВ ================");
        System.out.printf("[ПОЧАТКОВА МАТРИЦЯ X0 (%dx%d | Норма: %.4f | Слід: %.2f | det: %.2f)]:%n",
                x0.getRows(), x0.getCols(), x0.getFrobeniusNorm(), x0.getTrace(), x0.getDeterminant());
        printMatrix(x0);

        System.out.println();
        System.out.println("--- ПРЯМИЙ ХІД КОНВЕЄРА (Forward Pass через final execute()) ---");
        AbstractMatrixOperator[] forward = {
                new NonZeroScaleOperator(2.0),
                new TransposeOperator(),
                new GivensRotationOperator(Math.toRadians(60.0))
        };

        ImmutableMatrix2D current = x0;
        for (int i = 0; i < forward.length; i++) {
            AbstractMatrixOperator op = forward[i];
            System.out.printf("%d. Оператор '%s'", i + 1, op.getName());
            current = op.execute(current);
            if (op instanceof OrthogonalOperator) {
                System.out.printf(" -> det: %.2f%n", current.getDeterminant());
            } else {
                System.out.printf(" -> Норма: %.4f | det: %.2f%n",
                        current.getFrobeniusNorm(), current.getDeterminant());
            }
        }

        System.out.printf("[ЗАШИФРОВАНА МАТРИЦЯ Y ПІСЛЯ %d ЕТАПІВ]:%n", forward.length);
        printMatrix(current);

        System.out.println();
        System.out.println("--- ЗВОРОТНИЙ ХІД ВІДНОВЛЕННЯ (Inverse Pass через instanceof InvertibleOperator) ---");
        for (int i = forward.length - 1; i >= 0; i--) {
            if (forward[i] instanceof InvertibleOperator inv) {
                current = inv.applyInverse(current);
            }
        }

        System.out.println();
        System.out.println("[ВІДНОВЛЕНА МАТРИЦЯ X_restored]:");
        printMatrix(current);

        double error = x0.maxAbsDifference(current);
        System.out.printf("Максимальна похибка відновлення |X0 - X_restored|: %.2e %s%n", error,
                error < 1e-12 ? "(БЕЗВТРАТНА ОБОРОТНІСТЬ ДОТРИМАНА!)" : "(ВТРАТА ТОЧНОСТІ!)");
        System.out.println("==========================================================================");
    }
}
