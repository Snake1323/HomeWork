package practice04_homework;

import java.util.Scanner;

public class Task4PrimeFinder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ДЕТЕКТОР ПРОСТИХ ЧИСЕЛ ===");
        int n;
        do {
            System.out.print("Введіть верхню межу N: ");
            n = scanner.nextInt();
            if (n < 2) {
                System.out.println("Помилка: N має бути не меншим за 2!");
            }
        } while (n < 2);

        System.out.println();
        System.out.println("Знайдені прості числа у проміжку від 2 до " + n + ":");

        StringBuilder primes = new StringBuilder();
        int cnt = 0;
        int sum = 0;

        for (int num = 2; num <= n; num++) {
            boolean isPrime = true;
            for (int d = 2; d * d <= num; d++) {
                if (num % d == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                if (cnt > 0) {
                    primes.append(", ");
                }
                primes.append(num);
                cnt++;
                sum += num;
            }
        }

        System.out.println(primes.toString());
        System.out.println();
        System.out.println("--- СТАТИСТИКА ---");
        System.out.println("Усього чисел у діапазоні:    " + (n - 1));
        System.out.println("Знайдено простих чисел:      " + cnt + " шт.");
        System.out.println("Сума всіх простих чисел:     " + sum);
        double percent = ((double) cnt / (n - 1)) * 100;
        System.out.printf(java.util.Locale.US, "Частка простих чисел:        %.2f%%%n", percent);
        System.out.println("==============================");

        scanner.close();
    }
}

