package HomeWork_5;

public class Task2 {
    public static void main(String[] args) {

        String[] items = {
                "Ноутбук",
                "Смартфон",
                "Бездротові навушники",
                "Механічна клавіатура",
                "Монітор 27"
        };

        int[] quantities = {8, 3, 25, 4, 12};

        double[] prices = {
                28500.0,
                19200.0,
                1850.0,
                3100.0,
                9400.0
        };

        double[] totalValues = new double[items.length];

        for (int i = 0; i < items.length; i++) {
            totalValues[i] = quantities[i] * prices[i];
        }

        double total = 0;

        for (int i = 0; i < totalValues.length; i++) {
            total = total + totalValues[i];
        }

        int maxIndex = 0;

        for (int i = 1; i < totalValues.length; i++) {
            if (totalValues[i] > totalValues[maxIndex]) {
                maxIndex = i;
            }
        }

        System.out.println("ЗВІТ СКЛАДУ");

        for (int i = 0; i < items.length; i++) {
            System.out.println((i + 1) + ". " + items[i] + " | Кількість: " + quantities[i] + " | Ціна: " + prices[i] + " | Загальна вартість: " + totalValues[i]);
        }

        System.out.println();
        System.out.println("Загальна вартість складу: " + total + " грн");

        System.out.println("Найбільший капітал у товарі: " + items[maxIndex] + " - " + totalValues[maxIndex] + " грн");

        System.out.println();
        System.out.println("Товари, яких залишилось менше 5: ");

        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] < 5) {
                System.out.println(
                        items[i] + " - залишилось " +  quantities[i] + " шт. УВАГА: Терміново замовити постачальнику!"
                );
            }
        }
    }
}
