package HomeWork_2;

import java.util.Scanner;

public class Task4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("ПАНЕЛЬ КЛІМАТ-КОНТРОЛЮ SMART HOME ");
        System.out.print("Поточна температура (°C): ");
        double a = scanner.nextDouble();
        System.out.print("Бажана температура (°C): ");
        double b = scanner.nextDouble();
        System.out.print("Вологість повітря (%): ");
        int c = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Сезон (зима/літо/міжсезоння): ");
        String d = scanner.nextLine();
        System.out.print("Режим будинку (1-Standby, 2-Eco, 3-Party, 4-Night, 5-Vacation): ");
        int e = scanner.nextInt();
        System.out.print("Чи відчинені вікна? (true/false): ");
        boolean f = scanner.nextBoolean();

        boolean blocked = f && (e != 5);
        if (blocked) {
            System.out.println("ПОПЕРЕДЖЕННЯ: Відчинені вікна! Обігрів та кондиціонування примусово заблоковано!");
        }

        double t = switch (e) {
            case 1 -> b;
            case 2 -> {
                double k = 0.0;
                if (d.equalsIgnoreCase("зима")) {
                    k = -2.0;
                } else if (d.equalsIgnoreCase("літо")) {
                    k = 2.0;
                }
                yield b + k;
            }
            case 3 -> b - 1.5;
            case 4 -> 19.0;
            case 5 -> {
                if (d.equalsIgnoreCase("зима")) {
                    yield 12.0;
                } else {
                    yield 28.0;
                }
            }
            default -> b;
        };

        String modeName = switch (e) {
            case 1 -> "Standby (Звичайний)";
            case 2 -> "Eco (Енергозбереження)";
            case 3 -> "Party (Гості)";
            case 4 -> "Night (Ніч)";
            case 5 -> "Vacation (Відпустка)";
            default -> "Невідомий режим";
        };

        String gen = "міжсезоння";
        if (d.equalsIgnoreCase("зима")) {
            gen = "зими";
        } else if (d.equalsIgnoreCase("літо")) {
            gen = "літа";
        }
        String note = (t != b) ? " (скориговано з " + b + "°C для " + gen + ")" : "";

        String reg;
        if (blocked) {
            reg = "Клімат-контроль ВИМКНЕНО (вікна відчинені)";
        } else if (a - t > 0.5) {
            reg = "Кондиціонер УВІМКНЕНО (Охолодження)";
        } else if (t - a > 0.5) {
            reg = "Опалення УВІМКНЕНО (Обігрів)";
        } else {
            reg = "Температура в зоні комфорту (Кліматичні прилади в очікуванні)";
        }

        String hum = (c < 40) ? "Зволожувач УВІМКНЕНО (вологість " + c + "% < 40%)"
                : (c > 65) ? "Осушувач УВІМКНЕНО (вологість " + c + "% > 65%)"
                : "Вологість оптимальна";

        String energy = blocked ? "Зупинено"
                : ((e == 2 || e == 4 || e == 5) ? "Оптимізоване" : "Звичайне");

        System.out.println("------------- СТАТУС СИСТЕМИ -------------");
        System.out.printf("%-27s%s%n", "Режим роботи:", modeName);
        System.out.printf("%-27s%.2f°C%s%n", "Цільова температура:", t, note);
        System.out.printf("%-27s%.2f°C%n", "Поточна температура:", a);
        System.out.printf("%-27s%s%n", "Стан терморегуляції:", reg);
        System.out.printf("%-27s%s%n", "Стан мікроклімату:", hum);
        System.out.printf("%-27s%s%n", "Енергоспоживання:", energy);
        scanner.close();
    }
}
