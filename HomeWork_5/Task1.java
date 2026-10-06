package HomeWork_5;

import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] days = {
                "Понеділок", "Вівторок", "Середа",
                "Четвер", "П'ятниця", "Субота", "Неділя"
        };

        double[] sales = new double[7];

        for (int i = 0; i < sales.length; i++) {
            System.out.print("Введіть виторг за " + days[i] + ": ");
            sales[i] = scanner.nextDouble();
        }

        double total = 0;

        for (int i = 0; i < sales.length; i++) {
            total = total + sales[i];
        }

        double average = total / sales.length;

        double max = sales[0];
        double min = sales[0];

        int maxDay = 0;
        int minDay = 0;

        for (int i = 1; i < sales.length; i++) {
            if (sales[i] > max) {
                max = sales[i];
                maxDay = i;
            }

            if (sales[i] < min) {
                min = sales[i];
                minDay = i;
            }
        }

        System.out.println();
        System.out.println("Загальний виторг: " + total + " грн");
        System.out.println("Середній виторг: " + average + " грн");
        System.out.println("Найкращий день: " + days[maxDay] + " - " + max + " грн");
        System.out.println("Найгірший день: " + days[minDay] + " - " + min + " грн");

        System.out.println();
        System.out.println("Дні з виторгом вище середнього:");

        for (int i = 0; i < sales.length; i++) {
            if (sales[i] > average) {
                System.out.println(days[i] + ": " + sales[i] + " грн");
            }
        }

        scanner.close();
    }
}
