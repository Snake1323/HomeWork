package semester1.practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1WrapperAndAutoboxingMemory {
    public static void main(String[] args) {
        System.out.println("Завдання 1");
        inspectIntegerCache(new int[]{-128, -50, 0, 127, 128, 500, 2026});
        demonstrateAutoboxingUnderTheHood();
        String[] packets = {"  145 ", "-30", "ERR_404", "980", "", null, "2147483647", "-85"};
        printExtremesAndStats(parseTelemetryStream(packets));
    }

    public static void inspectIntegerCache(int[] testValues) {
        System.out.println("Число | == | equals");
        for (int value : testValues) {
            Integer first = Integer.valueOf(value);
            Integer second = Integer.valueOf(value);
            System.out.println(value + " | " + (first == second) + " | " + first.equals(second));
        }
    }

    public static void demonstrateAutoboxingUnderTheHood() {
        Integer number = 42;
        Integer explicitNumber = Integer.valueOf(42);
        int n = number;
        int explicitN = explicitNumber.intValue();
        System.out.println("Integer: " + n + " | " + explicitN);

        Double decimal = 3.5;
        Double explicitDecimal = Double.valueOf(3.5);
        double d = decimal;
        double explicitD = explicitDecimal.doubleValue();
        System.out.println("Double: " + d + " | " + explicitD);

        Boolean flag = true;
        Boolean explicitFlag = Boolean.valueOf(true);
        boolean b = flag;
        boolean explicitB = explicitFlag.booleanValue();
        System.out.println("Boolean: " + b + " | " + explicitB);

        List<Integer> numbers = Arrays.asList(45, null, -12, 88, null, 102);
        int sum = 0;
        for (Integer value : numbers) {
            if (value != null) {
                sum += value;
            } else {
                System.out.println("Пропущено null");
            }
        }
        System.out.println("Сума: " + sum);
    }

    public static List<Integer> parseTelemetryStream(String[] rawPackets) {
        List<Integer> result = new ArrayList<>();
        for (String packet : rawPackets) {
            if (packet == null || packet.trim().isEmpty()) {
                System.out.println("Порожній або null-пакет пропущено");
                continue;
            }
            try {
                result.add(Integer.parseInt(packet.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Неправильний пакет: " + packet);
            }
        }
        return result;
    }

    public static void printExtremesAndStats(List<Integer> validData) {
        if (validData.isEmpty()) {
            System.out.println("Немає чисел");
            return;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0;
        for (int value : validData) {
            if (value < min) min = value;
            if (value > max) max = value;
            sum += value;
        }
        System.out.println("Числа: " + validData);
        System.out.println("Мінімум: " + min + ", максимум: " + max + ", сума: " + sum);
        validData.remove(Integer.valueOf(-30));
        System.out.println("Після видалення значення -30: " + validData);
        validData.remove(0);
        System.out.println("Після видалення за індексом 0: " + validData);
    }
}
