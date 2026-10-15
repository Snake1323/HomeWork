package semester1.practice06.homework;

class MatrixSolver {
    private double[][] data;

    public void setMatrix(double[][] data) {
        this.data = data;
    }

    public void printMatrix() {
        if (data == null) return;
        for (double[] row : data) {
            System.out.print("[ ");
            for (double v : row) {
                System.out.printf(java.util.Locale.US, "%6.2f ", v);
            }
            System.out.println("]");
        }
    }

    public double getDeterminant2x2() {
        return data[0][0] * data[1][1] - data[0][1] * data[1][0];
    }

    public double getDeterminant3x3() {
        return data[0][0] * data[1][1] * data[2][2]
                + data[0][1] * data[1][2] * data[2][0]
                + data[0][2] * data[1][0] * data[2][1]
                - data[0][2] * data[1][1] * data[2][0]
                - data[0][0] * data[1][2] * data[2][1]
                - data[0][1] * data[1][0] * data[2][2];
    }

    public void transpose() {
        int rows = data.length;
        int cols = data[0].length;
        double[][] t = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                t[j][i] = data[i][j];
            }
        }
        data = t;
    }

    public void solveSystem2x2(double c1, double c2) {
        double delta = getDeterminant2x2();
        double dx = c1 * data[1][1] - c2 * data[0][1];
        double dy = data[0][0] * c2 - data[1][0] * c1;

        System.out.printf(java.util.Locale.US, "Головний визначник Delta  = %.2f%n", delta);
        System.out.printf(java.util.Locale.US, "Визначник Delta_x        = %.2f%n", dx);
        System.out.printf(java.util.Locale.US, "Визначник Delta_y        = %.2f%n", dy);
        System.out.println();

        if (Math.abs(delta) > 1e-9) {
            double x = dx / delta;
            double y = dy / delta;
            System.out.println("Знайдено єдиний розв'язок:");
            System.out.printf(java.util.Locale.US, "  x = Delta_x / Delta = %.2f%n", x);
            System.out.printf(java.util.Locale.US, "  y = Delta_y / Delta = %.2f%n", y);
            double left = data[0][0] * x + data[0][1] * y;
            System.out.printf(java.util.Locale.US, "Перевірка: %.0f*(%.0f) + %.0f*(%.0f) = %.0f (ВІРНО!)%n", data[0][0], x, data[0][1], y, left);
        } else if (Math.abs(dx) < 1e-9 && Math.abs(dy) < 1e-9) {
            System.out.println("Система має безліч розв'язків");
        } else {
            System.out.println("Система несумісна (не має розв'язків)");
        }
    }
}

public class Task4MatrixSolver {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = = = = = = = МАТРИЧНИЙ КАЛЬКУЛЯТОР ТА СЛАР = = = = = = = = = = = = = =");

        MatrixSolver ms = new MatrixSolver();
        double[][] a = {{2.0, 3.0}, {4.0, -1.0}};
        ms.setMatrix(a);

        System.out.println("Початкова матриця 2x2 коефіцієнтів A:");
        ms.printMatrix();
        System.out.println();
        System.out.printf(java.util.Locale.US, "Визначник det(A) = %.2f%n", ms.getDeterminant2x2());
        System.out.println();

        ms.transpose();
        System.out.println("Транспонована матриця A^T:");
        ms.printMatrix();
        System.out.println();

        ms.setMatrix(a);
        System.out.println("--- РОЗВ'ЯЗАННЯ СИСТЕМИ ЛІНІЙНИХ РІВНЯНЬ (МЕТОД КРАМЕРА) ---");
        System.out.println("Рівняння:");
        System.out.println("  2.00*x + 3.00*y = 8.00");
        System.out.println("  4.00*x - 1.00*y = 2.00");
        System.out.println();
        ms.solveSystem2x2(8.0, 2.0);

        System.out.println();
        double[][] c = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        ms.setMatrix(c);
        System.out.printf(java.util.Locale.US, "Визначник матриці 3x3 = %.2f%n", ms.getDeterminant3x3());
        System.out.println("===============================================================================");
    }
}
