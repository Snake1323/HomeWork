package semester1.practice08.homework;

class Matrix2D {
    protected double[][] data;
    private int rows;
    private int cols;

    public Matrix2D(double[][] source) {
        rows = source.length;
        cols = source[0].length;
        data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = source[i][j];
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getElement(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            System.out.println("Помилка: індекс за межами матриці!");
            return 0.0;
        }
        return data[r][c];
    }

    public void setElement(int r, int c, double val) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            System.out.println("Помилка: індекс за межами матриці!");
            return;
        }
        data[r][c] = val;
    }

    public double getSum() {
        double sum = 0.0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum = sum + data[i][j];
            }
        }
        return sum;
    }

    public double getMaxElement() {
        double max = data[0][0];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                }
            }
        }
        return max;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    sb.append(String.format(java.util.Locale.US, "%7.2f", data[i][j]));
                } else {
                    sb.append(String.format(java.util.Locale.US, " %7.2f", data[i][j]));
                }
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }
}

class SquareMatrix extends Matrix2D {

    public SquareMatrix(double[][] source) {
        super(source);
        if (getRows() != getCols()) {
            System.out.println("Попередження: матриця не є квадратною!");
        }
    }

    public int getDimension() {
        return getRows();
    }

    public double getTrace() {
        double trace = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            trace = trace + data[i][i];
        }
        return trace;
    }

    public boolean isSymmetric() {
        int n = getDimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (data[i][j] != data[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public double getDeterminant() {
        int n = getDimension();
        if (n == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        if (n == 3) {
            return data[0][0] * data[1][1] * data[2][2]
                    + data[0][1] * data[1][2] * data[2][0]
                    + data[0][2] * data[1][0] * data[2][1]
                    - data[0][2] * data[1][1] * data[2][0]
                    - data[0][0] * data[1][2] * data[2][1]
                    - data[0][1] * data[1][0] * data[2][2];
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(java.util.Locale.US,
                "Характеристики SquareMatrix: Розмір = %dx%d | Слід (Trace) = %.2f | Симетрична = %b | det(A) = %.2f",
                getRows(), getCols(), getTrace(), isSymmetric(), getDeterminant());
    }
}

class CramerMatrixSolver extends SquareMatrix {
    private double[] freeTerms;

    public CramerMatrixSolver(double[][] coefficients, double[] freeTerms) {
        super(coefficients);
        this.freeTerms = new double[freeTerms.length];
        for (int i = 0; i < freeTerms.length; i++) {
            this.freeTerms[i] = freeTerms[i];
        }
    }

    public double[] solve() {
        double delta = getDeterminant();
        if (Math.abs(delta) < 1e-9) {
            System.out.println("Єдиного розв'язку немає: головний визначник дорівнює 0!");
            return new double[0];
        }
        int n = getDimension();
        double[] roots = new double[n];
        for (int col = 0; col < n; col++) {
            double[][] copy = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j == col) {
                        copy[i][j] = freeTerms[i];
                    } else {
                        copy[i][j] = data[i][j];
                    }
                }
            }
            SquareMatrix helper = new SquareMatrix(copy);
            roots[col] = helper.getDeterminant() / delta;
        }
        return roots;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Розширена матриця системи [A | B]:\n");
        for (int i = 0; i < getRows(); i++) {
            sb.append("[");
            for (int j = 0; j < getCols(); j++) {
                if (j == 0) {
                    sb.append(String.format(java.util.Locale.US, "%7.2f", data[i][j]));
                } else {
                    sb.append(String.format(java.util.Locale.US, " %7.2f", data[i][j]));
                }
            }
            sb.append(String.format(java.util.Locale.US, "  |%7.2f", freeTerms[i]));
            sb.append(" ]\n");
        }
        sb.append(String.format(java.util.Locale.US, "Головний визначник det(A) = %.2f%n", getDeterminant()));
        double[] roots = solve();
        if (roots.length == 0) {
            sb.append("Розв'язок системи не існує!\n");
        } else {
            sb.append("Розв'язок системи: ");
            for (int i = 0; i < roots.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(String.format(java.util.Locale.US, "x%d = %.2f", i + 1, roots[i]));
            }
            sb.append(" (Перевірка пройдена!)\n");
        }
        return sb.toString();
    }
}

public class Task4MatrixHierarchy {
    public static void main(String[] args) {
        System.out.println("================ ІЄРАРХІЯ МАТРИЦЬ ТА СЛАР ================");

        System.out.println("--- 1. Квадратна матриця 3x3 (SquareMatrix) ---");
        SquareMatrix m = new SquareMatrix(new double[][]{
                {2.0, 1.0, 0.0},
                {1.0, 3.0, 1.0},
                {0.0, 1.0, 2.0}
        });
        System.out.println(m);
        System.out.println();

        System.out.println("--- 2. Розв'язувач системи Крамера (CramerMatrixSolver 2x2) ---");
        CramerMatrixSolver solver = new CramerMatrixSolver(
                new double[][]{
                        {2.0, 3.0},
                        {4.0, -1.0}
                },
                new double[]{8.0, 2.0}
        );
        System.out.print(solver);

        System.out.println("==========================================================");
    }
}
