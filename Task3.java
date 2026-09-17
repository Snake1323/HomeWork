package HomeWork_1;

public class Task3 {
    public static void main(String[] args) {
        double a = 3.0;
        double b = 4.0;
        double c = 5.0;

        boolean exists = (a + b > c) && (a + c > b) && (b + c > a);
        boolean isEqual = (a == b) && (b == c);
        boolean isIso = (a == b) || (b == c) || (a == c);

        boolean isRight = (Math.pow(a, 2) + Math.pow(b, 2) == Math.pow(c, 2)) ||
                (Math.pow(a, 2) + Math.pow(c, 2) == Math.pow(b, 2)) ||
                (Math.pow(b, 2) + Math.pow(c, 2) == Math.pow(a, 2));

        System.out.println("Сторони: a=" + a + ", b=" + b + ", c=" + c);
        System.out.println("Існує: " + exists);
        System.out.println("Рівносторонній: " + isEqual);
        System.out.println("Рівнобедрений: " + isIso);
        System.out.println("Прямокутний: " + isRight);
    }
}
