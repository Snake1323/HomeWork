package semester1.practice06.homework;

class Fraction {
    private int numerator;
    private int denominator;

    public void setValues(int num, int den) {
        if (den == 0) {
            System.out.println("Помилка: знаменник не може дорівнювати 0!");
            denominator = 1;
            numerator = num;
            return;
        }
        if (den < 0) {
            numerator = -num;
            denominator = -den;
        } else {
            numerator = num;
            denominator = den;
        }
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public void simplify() {
        int g = gcd(numerator, denominator);
        if (g != 0) {
            numerator /= g;
            denominator /= g;
        }
    }

    public double toDecimal() {
        return (double) numerator / denominator;
    }

    public void add(int otherNum, int otherDen) {
        int newNum = numerator * otherDen + otherNum * denominator;
        int newDen = denominator * otherDen;
        numerator = newNum;
        denominator = newDen;
        simplify();
    }

    public void printFraction() {
        if (denominator == 1) {
            System.out.print(numerator);
        } else {
            System.out.print(numerator + "/" + denominator);
        }
    }
}

public class Task1Fraction {
    public static void main(String[] args) {
        System.out.println("=== КАЛЬКУЛЯТОР ЗВИЧАЙНИХ ДРОБІВ ===");

        Fraction f = new Fraction();
        f.setValues(8, 12);
        System.out.println("Дріб 1: 8/12");
        f.simplify();
        System.out.print("Після скорочення: ");
        f.printFraction();
        System.out.printf(java.util.Locale.US, " (десятковий вигляд: %.4f)%n", f.toDecimal());
        System.out.println();

        System.out.println("Додаємо дріб 3/4 до 2/3...");
        System.out.println("Формула: 2/3 + 3/4 = (2*4 + 3*3) / (3*4) = 17/12");

        Fraction current = new Fraction();
        current.setValues(2, 3);
        current.add(3, 4);
        System.out.print("Результат додавання: ");
        current.printFraction();
        System.out.printf(java.util.Locale.US, " (десятковий вигляд: %.4f)%n", current.toDecimal());
        System.out.println("=====================================");
    }
}
