package practice04_homework;

import java.util.Scanner;

public class Task2DigitAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== АНАЛІЗАТОР РОЗРЯДІВ ЧИСЛА ===");
        System.out.print("Введіть додатне ціле число: ");
        int number = scanner.nextInt();

        int x = number;
        int cnt = 0;
        int sum = 0;
        int max = -1;
        int reversed = 0;

        while (x > 0) {
            int digit = x % 10;
            cnt++;
            sum += digit;
            if (digit > max) {
                max = digit;
            }
            reversed = reversed * 10 + digit;
            x /= 10;
        }

        if (cnt == 0) {
            cnt = 1;
            max = 0;
        }

        System.out.println();
        System.out.println("--- СТАТИСТИКА ЧИСЛА ---");
        System.out.println("Початкове число:     " + number);
        System.out.println("Кількість цифр:      " + cnt);
        System.out.println("Сума цифр:           " + sum);
        System.out.println("Найбільша цифра:     " + max);
        System.out.println("Перевернуте число:   " + reversed);
        if (number == reversed) {
            System.out.println("Результат:           Це число є паліндромом! ✨");
        } else {
            System.out.println("Результат:           Це звичайне число (не паліндром).");
        }
        System.out.println("================================");

        scanner.close();
    }
}

