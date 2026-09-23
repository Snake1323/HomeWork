package HomeWork_2;

import java.util.Scanner;

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== СИСТЕМА БАНКІВСЬКОГО СКОРИНГУ ===");
        System.out.print("Введіть вік позичальника: ");
        int a = scanner.nextInt();
        System.out.print("Введіть офіційний місячний дохід (грн): ");
        double b = scanner.nextDouble();
        System.out.print("Чи є негативна кредитна історія? (true/false): ");
        boolean c = scanner.nextBoolean();
        System.out.print("Введіть запитувану суму кредиту (грн): ");
        double d = scanner.nextDouble();
        System.out.print("Введіть бажаний термін (місяців): ");
        int e = scanner.nextInt();

        boolean okAge = (a >= 21 && a <= 65);
        boolean okHist = !c;
        boolean okPay = (e > 0) && ((d / e) <= (b * 0.5));
        double rate = (b >= 35000.0) ? 14.5 : 21.0;

        System.out.println("------------- РІШЕННЯ СКОРИНГУ -------------");
        if (okAge && okHist && okPay) {
            double pay = d / e;
            double proc = pay / b * 100;
            System.out.printf("%-29s%s%n", "Статус заявки:", "СХВАЛЕНО ");
            System.out.printf("%-29s%.2f грн (%.1f%% від доходу)%n", "Орієнтовний платіж/міс:", pay, proc);
            System.out.printf("%-29s%.1f%% річних%n", "Персональна ставка:", rate);
            System.out.printf("%s %.2f грн/міс%n", "Максимально допустимий платіж:", b * 0.5);
        } else {
            System.out.printf("%-29s%s%n", "Статус заявки:", "ВІДХИЛЕНО ");
            if (!okAge) {
                System.out.println("Відмова: вік не відповідає критеріям (21-65 років)");
            } else if (!okHist) {
                System.out.println("Відмова: виявлено негативну кредитну історію");
            } else if (e <= 0) {
                System.out.println("Відмова: некоректний термін кредиту");
            } else {
                System.out.println("Відмова: термін кредиту некоректний або щомісячний платіж перевищує 50% доходу");
            }
        }

        scanner.close();
    }
}
