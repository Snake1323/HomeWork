package semester1.practice03.homework;

import java.util.Scanner;

public class Task3CreditScoring {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(java.util.Locale.US);

        System.out.println("=== СИСТЕМА БАНКІВСЬКОГО СКОРИНГУ ===");
        System.out.print("Введіть вік позичальника: ");
        int age = sc.nextInt();
        System.out.print("Введіть офіційний місячний дохід (грн): ");
        double monthlyIncome = sc.nextDouble();
        System.out.print("Чи є негативна кредитна історія? (true/false): ");
        boolean hasBadCreditHistory = sc.nextBoolean();
        System.out.print("Введіть запитувану суму кредиту (грн): ");
        double loanAmount = sc.nextDouble();
        System.out.print("Введіть бажаний термін (місяців): ");
        int loanMonths = sc.nextInt();

        boolean ageOk = (age >= 21 && age <= 65);
        boolean historyOk = !hasBadCreditHistory;
        boolean paymentOk = (loanMonths > 0) && ((loanAmount / loanMonths) <= (monthlyIncome * 0.5));
        boolean approved = ageOk && historyOk && paymentOk;

        double rate = (monthlyIncome >= 35000.0) ? 14.5 : 21.0;

        System.out.println();
        System.out.println("------------- РІШЕННЯ СКОРИНГУ -------------");
        if (approved) {
            double payment = loanAmount / loanMonths;
            double percent = (monthlyIncome > 0) ? ((payment / monthlyIncome) * 100.0) : 0.0;
            double maxPayment = monthlyIncome * 0.5;
            System.out.println("Статус заявки:               СХВАЛЕНО ✅");
            System.out.printf(java.util.Locale.US, "Орієнтовний платіж/міс:      %.2f грн (%.1f%% від доходу)%n", payment, percent);
            System.out.printf(java.util.Locale.US, "Персональна ставка:          %.1f%% річних%n", rate);
            System.out.printf(java.util.Locale.US, "Максимально допустимий платіж: %.2f грн/міс%n", maxPayment);
        } else {
            System.out.println("Статус заявки:               ВІДХИЛЕНО ❌");
            if (!ageOk) {
                System.out.println("Відмова: вік не відповідає критеріям (21-65 років)");
            } else if (hasBadCreditHistory) {
                System.out.println("Відмова: виявлено негативну кредитну історію");
            } else {
                System.out.println("Відмова: термін кредиту некоректний або щомісячний платіж перевищує 50% доходу");
            }
        }
        System.out.println("============================================");

        sc.close();
    }
}

