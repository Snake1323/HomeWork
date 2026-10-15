package practice04_homework;

import java.util.Scanner;

public class Task1RangeStatistics {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== АНАЛІЗАТОР ДІАПАЗОНУ ЧИСЕЛ ===");
        int n;
        do {
            System.out.print("Введіть число N: ");
            n = scanner.nextInt();
            if (n <= 0) {
                System.out.println("Помилка: N має бути більшим за 0!");
            }
        } while (n <= 0);

        int sum = 0;
        int sumEven = 0;
        int sumOdd = 0;
        int cntEven = 0;
        int cntOdd = 0;

        for (int i = 1; i <= n; i++) {
            sum += i;
            if (i % 2 == 0) {
                sumEven += i;
                cntEven++;
            } else {
                sumOdd += i;
                cntOdd++;
            }
        }

        double average = (double) sum / n;

        System.out.println();
        System.out.println("--- РЕЗУЛЬТАТИ ДЛЯ ДІАПАЗОНУ [1 .. " + n + "] ---");
        System.out.println("Загальна сума чисел:       " + sum);
        System.out.println("Сума парних чисел:         " + sumEven + " (кількість: " + cntEven + ")");
        System.out.println("Сума непарних чисел:       " + sumOdd + " (кількість: " + cntOdd + ")");
        System.out.printf(java.util.Locale.US, "Середнє арифметичне:       %.2f%n", average);
        System.out.println("=========================================");

        scanner.close();
    }
}

