package semester1.practice03.homework;

import java.util.Scanner;

public class Task4SmartHomeClimate {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(java.util.Locale.US);

        System.out.println("=== ПАНЕЛЬ КЛІМАТ-КОНТРОЛЮ SMART HOME ===");
        System.out.print("Поточна температура (°C): ");
        double currentTemp = sc.nextDouble();
        System.out.print("Бажана температура (°C): ");
        double desiredTemp = sc.nextDouble();
        System.out.print("Вологість повітря (%): ");
        int humidity = sc.nextInt();
        sc.nextLine();
        System.out.print("Сезон (зима/літо/міжсезоння): ");
        String season = sc.nextLine();
        System.out.print("Режим будинку (1-Standby, 2-Eco, 3-Party, 4-Night, 5-Vacation): ");
        int mode = sc.nextInt();
        System.out.print("Чи відчинені вікна? (true/false): ");
        boolean isWindowOpen = sc.nextBoolean();

        boolean blocked = isWindowOpen && (mode != 5);
        if (blocked) {
            System.out.println();
            System.out.println("ПОПЕРЕДЖЕННЯ: Відчинені вікна! Обігрів та кондиціонування примусово заблоковано!");
        }

        double effectiveTargetTemp = switch (mode) {
            case 1 -> desiredTemp;
            case 2 -> {
                double change = 0.0;
                if (season.equalsIgnoreCase("зима")) {
                    change = -2.0;
                } else if (season.equalsIgnoreCase("літо")) {
                    change = 2.0;
                }
                yield desiredTemp + change;
            }
            case 3 -> desiredTemp - 1.5;
            case 4 -> 19.0;
            case 5 -> {
                if (season.equalsIgnoreCase("зима")) {
                    yield 12.0;
                }
                yield 28.0;
            }
            default -> desiredTemp;
        };

        String modeName = switch (mode) {
            case 1 -> "Standby (Звичайний)";
            case 2 -> "Eco (Енергозбереження)";
            case 3 -> "Party (Гості)";
            case 4 -> "Night (Ніч)";
            case 5 -> "Vacation (Відпустка)";
            default -> "Невідомий";
        };

        String thermoStatus;
        if (blocked) {
            thermoStatus = "Кліматичні установки заблоковано";
        } else if (currentTemp - effectiveTargetTemp > 0.5) {
            thermoStatus = "Кондиціонер УВІМКНЕНО (Охолодження)";
        } else if (effectiveTargetTemp - currentTemp > 0.5) {
            thermoStatus = "Опалення УВІМКНЕНО (Обігрів)";
        } else {
            thermoStatus = "Температура в зоні комфорту (Кліматичні прилади в очікуванні)";
        }

        String humidityStatus = (humidity < 40)
                ? ("Зволожувач УВІМКНЕНО (вологість " + humidity + "% < 40%)")
                : ((humidity > 65) ? "Осушувач УВІМКНЕНО" : "Вологість оптимальна");

        String energy = (mode == 2) ? "Оптимізоване" : "Стандартне";

        System.out.println();
        System.out.println("------------- СТАТУС СИСТЕМИ -------------");
        System.out.printf(java.util.Locale.US, "Режим роботи:              %s%n", modeName);
        System.out.printf(java.util.Locale.US, "Цільова температура:       %.2f°C (скориговано з %.1f°C)%n", effectiveTargetTemp, desiredTemp);
        System.out.printf(java.util.Locale.US, "Поточна температура:       %.2f°C%n", currentTemp);
        System.out.println("Стан терморегуляції:       " + thermoStatus);
        System.out.println("Стан мікроклімату:         " + humidityStatus);
        System.out.println("Енергоспоживання:          " + energy);
        System.out.println("==========================================");

        sc.close();
    }
}

