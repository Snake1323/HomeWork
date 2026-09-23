package HomeWork_2;

import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("ТАРИФІКАТОР СЛУЖБИ ДОСТАВКИ");
        System.out.print("Введіть вагу відправлення (кг): ");
        double a = scanner.nextDouble();
        System.out.print("Введіть відстань транспортування (км): ");
        int b = scanner.nextInt();
        System.out.print("Оберіть пункт призначення (1 - Відділення, 2 - Поштомат, 3 - Кур'єр): ");
        int c = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Чи є у вас карта Premium? (так/ні): ");
        String d = scanner.nextLine();

        if (a <= 0 || a > 50.0) {
            System.out.println("Помилка: вага має бути більшою за 0 і не більшою за 50 кг!");
            scanner.close();
            return;
        }
        if (b <= 0) {
            System.out.println("Помилка: відстань має бути більшою за 0 км!");
            scanner.close();
            return;
        }

        double base = switch (c) {
            case 1 -> 50.0;
            case 2 -> {
                if (a > 15.0) {
                    System.out.println("Поштомат не підтримує вантажі понад 15 кг!");
                    yield -1;
                }
                yield 60.0;
            }
            case 3 -> 100.0;
            default -> {
                System.out.println("Помилка: невідомий тип доставки!");
                yield -1;
            }
        };

        if (base == -1) {
            scanner.close();
            return;
        }

        String s = switch (c) {
            case 1 -> "Відділення";
            case 2 -> "Поштомат";
            case 3 -> "Кур'єр";
            default -> "Невідомий";
        };

        double extra;
        if (b <= 50) {
            extra = 0.0;
        } else if (b <= 200) {
            extra = 35.0;
        } else {
            extra = 80.0;
        }

        boolean prem = d.equalsIgnoreCase("так");
        double sum = base + extra;
        double pay = prem ? sum * 0.8 : sum;

        System.out.println("------------- НАКЛАДНА ДОСТАВКИ -------------");
        System.out.printf("%-27s%s (базовий тариф: %.2f грн)%n", "Тип доставки:", s, base);
        System.out.printf("%-27s%.2f грн (%d км)%n", "Доплата за відстань:", extra, b);
        System.out.printf("%-27s%.2f грн%n", "Сума до знижки:", sum);
        System.out.printf("%-27s%s%n", "Статус клієнта:", prem ? "Premium (-20%)" : "Звичайний");
        System.out.println("---------------------------------------------");
        System.out.printf("%-27s%.2f грн%n", "РАЗОМ ДО СПЛАТИ:", pay);
        scanner.close();
    }
}
