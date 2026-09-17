package HomeWork_1;

public class Task1 {
    public static void main(String[] args) {
        double usd = 200.0;
        final double RATE = 44.88;
        final double COM = 1.5;

        double total = usd * RATE;
        double comSum = total * (COM / 100.0);
        double netSum = total - comSum;
        long cash = Math.round(netSum);

        System.out.println("Сума в USD: " + usd);
        System.out.println("Курс: " + RATE);
        System.out.println("Сума в UAH до комісії: " + total);
        System.out.println("Комісія: " + comSum);
        System.out.println("Чиста сума: " + netSum);
        System.out.println("До видачі готівкою: " + cash);
    }
}
