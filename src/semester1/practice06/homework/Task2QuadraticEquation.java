package semester1.practice06.homework;

class QuadraticEquation {
    private double a;
    private double b;
    private double c;

    public void setCoefficients(double a, double b, double c) {
        if (a == 0) {
            System.out.println("Увага: коефіцієнт 'a' не може дорівнювати 0 (це лінійне рівняння)!");
            a = 1.0;
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getDiscriminant() {
        return b * b - 4 * a * c;
    }

    public boolean hasRealRoots() {
        return getDiscriminant() >= 0;
    }

    public double getRoot1() {
        double d = getDiscriminant();
        if (d < 0) return Double.NaN;
        return (-b + Math.sqrt(d)) / (2 * a);
    }

    public double getRoot2() {
        double d = getDiscriminant();
        if (d < 0) return Double.NaN;
        return (-b - Math.sqrt(d)) / (2 * a);
    }

    public void printSolution() {
        double d = getDiscriminant();
        System.out.printf(java.util.Locale.US, "Рівняння: %.1f*x^2 + (%.1f)*x + %.1f = 0%n", a, b, c);
        System.out.printf(java.util.Locale.US, "Дискримінант D = %.2f%n", d);
        if (d > 0) {
            System.out.println("Рівняння має 2 дійсні корені:");
            System.out.printf(java.util.Locale.US, "  x1 = %.2f%n", getRoot1());
            System.out.printf(java.util.Locale.US, "  x2 = %.2f%n", getRoot2());
        } else if (d == 0) {
            System.out.println("Рівняння має один дійсний корінь:");
            System.out.printf(java.util.Locale.US, "  x = %.2f%n", -b / (2 * a));
        } else {
            System.out.println("Рівняння не має дійсних коренів у множині R.");
        }
    }
}

public class Task2QuadraticEquation {
    public static void main(String[] args) {
        System.out.println("=== РОЗВ'ЯЗУВАЧ КВАДРАТНИХ РІВНЯНЬ ===");

        QuadraticEquation q1 = new QuadraticEquation();
        System.out.println("Тест 1:");
        q1.setCoefficients(1, -5, 6);
        q1.printSolution();
        System.out.println("--------------------------------------");

        QuadraticEquation q2 = new QuadraticEquation();
        System.out.println("Тест 2:");
        q2.setCoefficients(1, -4, 4);
        q2.printSolution();
        System.out.println("--------------------------------------");

        QuadraticEquation q3 = new QuadraticEquation();
        System.out.println("Тест 3:");
        q3.setCoefficients(1, 2, 5);
        q3.printSolution();
        System.out.println("======================================");
    }
}
