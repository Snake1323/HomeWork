package semester1.practice03.homework;

import java.util.Scanner;

public class Task2DeliveryService {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(java.util.Locale.US);

        System.out.println("=== ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ ===");
        System.out.print("Введіть вагу відправлення (кг): ");
        double weight = sc.nextDouble();
        System.out.print("Введіть відстань транспортування (км): ");
        int distance = sc.nextInt();
        System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
        int deliveryType = sc.nextInt();
        sc.nextLine();
        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        String premiumInput = sc.nextLine();

        if (weight <= 0 || weight > 50.0) {
            System.out.println("Помилка: вага має бути > 0 та <= 50.0 кг!");
            sc.close();
            return;
        }
        if (distance <= 0) {
            System.out.println("Помилка: відстань має бути > 0 км!");
            sc.close();
            return;
        }

        double baseTariff = switch (deliveryType) {
            case 1 -> 50.0;
            case 2 -> {
                if (weight > 15.0) {
                    System.out.println("Поштомат не підтримує вантажі понад 15 кг!");
                    yield -1.0;
                }
                yield 60.0;
            }
            case 3 -> 100.0;
            default -> -1.0;
        };

        if (baseTariff < 0) {
            if (deliveryType != 1 && deliveryType != 2 && deliveryType != 3) {
                System.out.println("Помилка: некоректний тип доставки!");
            }
            sc.close();
            return;
        }

        String deliveryName = switch (deliveryType) {
            case 1 -> "Відділення";
            case 2 -> "Поштомат";
            case 3 -> "Кур'єр";
            default -> "Невідомий";
        };

        double distanceFee;
        if (distance <= 50) {
            distanceFee = 0.0;
        } else if (distance <= 200) {
            distanceFee = 35.0;
        } else {
            distanceFee = 80.0;
        }

        boolean isPremium = premiumInput.equalsIgnoreCase("так");
        double beforeDiscount = baseTariff + distanceFee;
        double finalCost = isPremium ? (beforeDiscount * 0.8) : beforeDiscount;

        System.out.println();
        System.out.println("------------- НАКЛАДНА ДОСТАВКИ -------------");
        System.out.printf(java.util.Locale.US, "Тип доставки:              %s (базовий тариф: %.2f грн)%n", deliveryName, baseTariff);
        System.out.printf(java.util.Locale.US, "Доплата за відстань:       %.2f грн (%d км)%n", distanceFee, distance);
        System.out.printf(java.util.Locale.US, "Сума до знижки:            %.2f грн%n", beforeDiscount);
        if (isPremium) {
            System.out.println("Статус клієнта:            Premium (-20%)");
        } else {
            System.out.println("Статус клієнта:            Звичайний (без знижки)");
        }
        System.out.println("---------------------------------------------");
        System.out.printf(java.util.Locale.US, "РАЗОМ ДО СПЛАТИ:           %.2f грн%n", finalCost);
        System.out.println("=============================================");

        sc.close();
    }
}

