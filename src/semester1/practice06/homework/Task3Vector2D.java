package semester1.practice06.homework;

class Vector2D {
    private double x;
    private double y;

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void multiplyByScalar(double k) {
        x = x * k;
        y = y * k;
    }

    public double dotProduct(double otherX, double otherY) {
        return x * otherX + y * otherY;
    }

    public boolean isPerpendicularTo(double otherX, double otherY) {
        return Math.abs(dotProduct(otherX, otherY)) < 1e-9;
    }

    public void normalize() {
        double len = getLength();
        if (len > 0) {
            x = x / len;
            y = y / len;
        }
    }

    public void printVector() {
        System.out.printf(java.util.Locale.US, "(%.2f; %.2f)", x, y);
    }
}

public class Task3Vector2D {
    public static void main(String[] args) {
        System.out.println("=== ВЕКТОРНИЙ АНАЛІЗАТОР (2D GEOMETRY) ===");

        Vector2D a = new Vector2D();
        a.set(3, 4);

        Vector2D b = new Vector2D();
        b.set(-4, 3);

        System.out.printf(java.util.Locale.US, "Вектор A: (%.2f; %.2f), Довжина |A| = %.2f%n", a.getX(), a.getY(), a.getLength());
        System.out.printf(java.util.Locale.US, "Вектор B: (%.2f; %.2f), Довжина |B| = %.2f%n", b.getX(), b.getY(), b.getLength());
        System.out.println();

        double dot = a.dotProduct(b.getX(), b.getY());
        System.out.printf(java.util.Locale.US, "Скалярний добуток A · B: %.2f%n", dot);
        boolean perp = a.isPerpendicularTo(b.getX(), b.getY());
        System.out.println("Чи є вектори перпендикулярними (кут 90°)? -> " + (perp ? "ТАК (true)" : "НІ (false)"));
        System.out.println();

        System.out.println("Нормалізація вектора A...");
        a.normalize();
        System.out.printf(java.util.Locale.US, "Одиничний вектор A: (%.2f; %.2f)%n", a.getX(), a.getY());
        System.out.printf(java.util.Locale.US, "Нова довжина вектора A: %.2f%n", a.getLength());
        System.out.println("==========================================");
    }
}
