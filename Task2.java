package HomeWork_1;

public class Task2 {
    public static void main(String[] args) {
        int num = 742;

        int n1 = num / 100;
        int n2 = (num / 10) % 10;
        int n3 = num % 10;

        int sum = n1 + n2 + n3;
        int mult = n1 * n2 * n3;
        int rev = n3 * 100 + n2 * 10 + n1;
        boolean isEven = sum % 2 == 0;

        System.out.println("Число: " + num);
        System.out.println("Сотні: " + n1 + ", Десятки: " + n2 + ", Одиниці: " + n3);
        System.out.println("Сума цифр: " + sum);
        System.out.println("Добуток цифр: " + mult);
        System.out.println("Перевернуте число: " + rev);
        System.out.println("Сума парна: " + isEven);
    }
}
