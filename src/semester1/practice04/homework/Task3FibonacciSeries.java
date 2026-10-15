package practice04_homework;

import java.util.Scanner;

public class Task3FibonacciSeries {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ЧИСЛА ФІБОНАЧЧІ ТА ЗОЛОТИЙ ПЕРЕТИН ===");
        int n;
        do {
            System.out.print("Скільки чисел згенерувати (N >= 3): ");
            n = scanner.nextInt();
            if (n < 3) {
                System.out.println("Помилка: N має бути не меншим за 3!");
            }
        } while (n < 3);

        int f1 = 1;
        int f2 = 1;

        System.out.println();
        System.out.printf(java.util.Locale.US, "№ %-2d | Число: %d%n", 1, f1);
        System.out.printf(java.util.Locale.US, "№ %-2d | Число: %d%n", 2, f2);

        double last = 0;
        for (int i = 3; i <= n; i++) {
            int next = f1 + f2;
            double ratio = (double) next / f2;
            last = ratio;
            System.out.printf(java.util.Locale.US, "№ %-2d | Число: %-10d | Відношення F(n)/F(n-1) = %.6f%n", i, next, ratio);
            f1 = f2;
            f2 = next;
        }

        System.out.println();
        System.out.printf(java.util.Locale.US, "Підсумок: знайдено наближення Золотого перетину: %.6f%n", last);
        System.out.printf(java.util.Locale.US, "Еталонне значення:                             1.618034%n");
        System.out.println("=========================================");

        scanner.close();
    }
}

