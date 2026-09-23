package HomeWork_2;

import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("КАЛЬКУЛЯТОР ПОЇЗДКИ ТА ПАЛЬНОГО");
        System.out.print("Введіть відстань поїздки (км): ");
        double a = scanner.nextDouble();
        System.out.print("Введіть середню витрату на 100 км (л): ");
        double b = scanner.nextDouble();
        System.out.print("Оберіть тип пального (1 - А-95, 2 - Дизель, 3 - Газ): ");
        int c = scanner.nextInt();

        if (a <= 0 || b <= 0) {
            System.out.println("Помилка: відстань і витрата пального мають бути більшими за 0!");
            scanner.close();
            return;
        }

        double p;
        String s;
        switch (c) {
            case 1:
                p = 56.50;
                s = "Бензин А-95";
                break;
            case 2:
                p = 52.80;
                s = "Дизель";
                break;
            case 3:
                p = 29.40;
                s = "Газ";
                break;
            default:
                System.out.println("Помилка: невідомий тип пального!");
                scanner.close();
                return;
        }

        double d = a * b / 100.0;
        double e = d * p;
        double f = (e >= 2000.0) ? e * 0.95 : e;

        System.out.println("---------------- ЗВІТ ПОЇЗДКИ ----------------");
        System.out.printf("%-29s%s (%.2f грн/л)%n", "Обране пальне:", s, p);
        System.out.printf("%-29s%.2f л%n", "Необхідно пального:", d);
        System.out.printf("%-29s%.2f грн%n", "Базова вартість:", e);
        System.out.printf("%-29s%.2f грн%n", "Знижка АЗС (5%):", e - f);
        System.out.println("----------------------------------------------");
        System.out.printf("%-29s%.2f грн%n", "Підсумкова сума до сплати:", f);
        scanner.close();
    }
}
