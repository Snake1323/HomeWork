package semester1.practice03.homework;

import java.util.Scanner;

public class Task1FuelCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(java.util.Locale.US);

        System.out.println("=== КАЛЬКУЛЯТОР ПОЇЗДКИ ТА ПАЛЬНОГО ===");
        System.out.print("Введіть відстань поїздки (км): ");
        double distance = sc.nextDouble();
        System.out.print("Введіть середню витрату на 100 км (л): ");
        double fuelConsumption = sc.nextDouble();
        System.out.print("Оберіть тип пального (1 - А-95, 2 - Дизель, 3 - Газ): ");
        int fuelType = sc.nextInt();

        if (distance <= 0 || fuelConsumption <= 0) {
            System.out.println("Помилка: відстань та витрата мають бути більшими за 0!");
            sc.close();
            return;
        }

        String fuelName = switch (fuelType) {
            case 1 -> "Бензин А-95";
            case 2 -> "Дизель";
            case 3 -> "Газ";
            default -> null;
        };

        double price = switch (fuelType) {
            case 1 -> 56.50;
            case 2 -> 52.80;
            case 3 -> 29.40;
            default -> 0.0;
        };

        if (fuelName == null) {
            System.out.println("Помилка: невідомий тип пального!");
            sc.close();
            return;
        }

        double needed = distance * fuelConsumption / 100.0;
        double base = needed * price;
        double discount = (base >= 2000.0) ? (base * 0.05) : 0.0;
        double finalCost = base - discount;

        System.out.println();
        System.out.println("---------------- ЗВІТ ПОЇЗДКИ ----------------");
        System.out.printf(java.util.Locale.US, "Обране пальне:               %s (%.2f грн/л)%n", fuelName, price);
        System.out.printf(java.util.Locale.US, "Необхідно пального:          %.2f л%n", needed);
        System.out.printf(java.util.Locale.US, "Базова вартість:             %.2f грн%n", base);
        System.out.printf(java.util.Locale.US, "Знижка АЗС (5%%):             %.2f грн%n", discount);
        System.out.println("----------------------------------------------");
        System.out.printf(java.util.Locale.US, "Підсумкова сума до сплати:   %.2f грн%n", finalCost);
        System.out.println("=============================================");

        sc.close();
    }
}

