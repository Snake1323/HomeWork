package semester1.practice09.homework;

class EncapsulatedMatrix {
    private final int rows;
    private final int cols;
    private final double[][] grid;

    public EncapsulatedMatrix(double[][] source) {
        this.rows = source.length;
        this.cols = source[0].length;
        this.grid = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.grid[i][j] = source[i][j];
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getGridCopy() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = grid[i][j];
            }
        }
        return copy;
    }

    public double getElement(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            System.out.println("Помилка: індекс за межами матриці!");
            return 0.0;
        }
        return grid[r][c];
    }

    public boolean isSquare() {
        return rows == cols;
    }

    public double getTrace() {
        double sum = 0.0;
        int n = Math.min(rows, cols);
        for (int i = 0; i < n; i++) {
            sum = sum + grid[i][i];
        }
        return sum;
    }

    public double getFrobeniusNorm() {
        double sum = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + grid[i][j] * grid[i][j];
            }
        }
        return Math.sqrt(sum);
    }

    public double getDeterminant() {
        if (!isSquare()) {
            return 0.0;
        }
        if (rows == 2) {
            return grid[0][0] * grid[1][1] - grid[0][1] * grid[1][0];
        }
        if (rows == 3) {
            return grid[0][0] * grid[1][1] * grid[2][2]
                    + grid[0][1] * grid[1][2] * grid[2][0]
                    + grid[0][2] * grid[1][0] * grid[2][1]
                    - grid[0][2] * grid[1][1] * grid[2][0]
                    - grid[0][0] * grid[1][2] * grid[2][1]
                    - grid[0][1] * grid[1][0] * grid[2][2];
        }
        return 0.0;
    }

    public String getSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append(rows).append("x").append(cols);
        sb.append(String.format(java.util.Locale.US, " | Норма Фробеніуса: %.2f", getFrobeniusNorm()));
        if (isSquare()) {
            sb.append(String.format(java.util.Locale.US, " | Слід: %.2f | det: %.2f", getTrace(), getDeterminant()));
        }
        return sb.toString();
    }

    public String getRowsText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("  [");
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    sb.append(String.format(java.util.Locale.US, "%7.2f", grid[i][j]));
                } else {
                    sb.append(String.format(java.util.Locale.US, " %7.2f", grid[i][j]));
                }
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Матриця (" + getSignature() + "):\n" + getRowsText();
    }
}

abstract class AbstractMatrixOperator {
    private final String operatorName;

    public AbstractMatrixOperator(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public abstract boolean isApplicable(EncapsulatedMatrix input);

    public abstract EncapsulatedMatrix apply(EncapsulatedMatrix input);

    public final EncapsulatedMatrix executeSafely(EncapsulatedMatrix input) {
        if (input != null && isApplicable(input)) {
            return apply(input);
        }
        System.out.println("Попередження: оператор '" + operatorName + "' не застосовний до цієї матриці!");
        return input;
    }
}

class ScaleMatrixOperator extends AbstractMatrixOperator {
    private final double scaleFactor;

    public ScaleMatrixOperator(double scaleFactor) {
        super(String.format(java.util.Locale.US, "Scale(x%.1f)", scaleFactor));
        this.scaleFactor = scaleFactor;
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) {
        return true;
    }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        double[][] source = input.getGridCopy();
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[0].length; j++) {
                source[i][j] = source[i][j] * scaleFactor;
            }
        }
        return new EncapsulatedMatrix(source);
    }
}

class TransposeMatrixOperator extends AbstractMatrixOperator {

    public TransposeMatrixOperator() {
        super("Transpose(A^T)");
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) {
        return true;
    }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        int rows = input.getRows();
        int cols = input.getCols();
        double[][] source = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                source[j][i] = input.getElement(i, j);
            }
        }
        return new EncapsulatedMatrix(source);
    }
}

class DiagonalShiftOperator extends AbstractMatrixOperator {
    private final double lambda;

    public DiagonalShiftOperator(double lambda) {
        super(String.format(java.util.Locale.US, "DiagonalShift(%+.1f*I)", lambda));
        this.lambda = lambda;
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) {
        return input.isSquare();
    }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        double[][] source = input.getGridCopy();
        for (int i = 0; i < source.length; i++) {
            source[i][i] = source[i][i] + lambda;
        }
        return new EncapsulatedMatrix(source);
    }
}

class MultiplyByMatrixOperator extends AbstractMatrixOperator {
    private final EncapsulatedMatrix rightMatrix;

    public MultiplyByMatrixOperator(EncapsulatedMatrix rightMatrix) {
        super("Multiply(A x B)");
        this.rightMatrix = rightMatrix;
    }

    @Override
    public boolean isApplicable(EncapsulatedMatrix input) {
        return input.getCols() == rightMatrix.getRows();
    }

    @Override
    public EncapsulatedMatrix apply(EncapsulatedMatrix input) {
        int rows = input.getRows();
        int inner = input.getCols();
        int cols = rightMatrix.getCols();
        double[][] source = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < inner; k++) {
                    sum = sum + input.getElement(i, k) * rightMatrix.getElement(k, j);
                }
                source[i][j] = sum;
            }
        }
        return new EncapsulatedMatrix(source);
    }
}

class MatrixPipeline {
    private final String pipelineName;
    private final AbstractMatrixOperator[] stages;
    private int stageCount;

    public MatrixPipeline(String pipelineName, int capacity) {
        this.pipelineName = pipelineName;
        this.stages = new AbstractMatrixOperator[Math.max(1, capacity)];
        this.stageCount = 0;
    }

    public void addStage(AbstractMatrixOperator op) {
        if (op == null) {
            System.out.println("Помилка: порожній оператор конвеєра!");
            return;
        }
        if (stageCount >= stages.length) {
            System.out.println("Помилка: конвеєр переповнений!");
            return;
        }
        stages[stageCount] = op;
        stageCount++;
    }

    public EncapsulatedMatrix runPipeline(EncapsulatedMatrix initial) {
        EncapsulatedMatrix current = initial;
        for (int i = 0; i < stageCount; i++) {
            current = stages[i].executeSafely(current);
            System.out.printf(java.util.Locale.US, "---> Етап %d: Оператор '%s' виконано успішно:%n",
                    i + 1, stages[i].getOperatorName());
            System.out.print(current);
            if (i < stageCount - 1) {
                System.out.println();
            }
        }
        return current;
    }
}

public class Task4MatrixOperatorPipeline {
    public static void main(String[] args) {
        System.out.println("================ КОНВЕЄР МАТРИЧНИХ ОПЕРАТОРІВ ================");

        double[][] source = {{1.0, 2.0}, {3.0, 4.0}};
        EncapsulatedMatrix x0 = new EncapsulatedMatrix(source);

        System.out.println("[ВХІДНА МАТРИЦЯ X0 (" + x0.getSignature() + ")]:");
        System.out.print(x0.getRowsText());

        source[0][0] = 999.0;
        System.out.println("Перевірка захисного копіювання: зовнішній source[0][0] = 999.0, але X0[0][0] = "
                + x0.getElement(0, 0));
        System.out.println();

        MatrixPipeline pipeline = new MatrixPipeline("Матричний конвеєр", 4);
        pipeline.addStage(new ScaleMatrixOperator(2.0));
        pipeline.addStage(new TransposeMatrixOperator());
        pipeline.addStage(new DiagonalShiftOperator(3.0));
        pipeline.addStage(new MultiplyByMatrixOperator(new EncapsulatedMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}})));

        pipeline.runPipeline(x0);

        System.out.println("=============================================================");
    }
}
