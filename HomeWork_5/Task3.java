package HomeWork_5;

import java.util.Scanner;

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] hall = new char[5][6];

        //  таблиця
        for (int i = 0; i < hall.length; i++) {
            for (int j = 0; j < hall[i].length; j++) {
                hall[i][j] = 'O';
            }
        }

        int choice = -1;

        while (choice != 0) {

            System.out.println();
            System.out.println("1 - Переглянути схему залу");
            System.out.println("2 - Забронювати місце");
            System.out.println("3 - Звіт та каса");
            System.out.println("0 - Завершити зміну");

            System.out.print("Ваш вибір: ");
            choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("1 2 3 4 5 6");

                    for (int i = 0; i < hall.length; i++) {
                        System.out.print("Р" + (i + 1) + ": ");

                        for (int j = 0; j < hall[i].length; j++) {
                            System.out.print(hall[i][j] + " ");
                        }

                        System.out.println();
                    }
                    break;

                case 2:
                    System.out.print("Введіть номер ряду (1-5): ");
                    int row = scanner.nextInt();

                    System.out.print("Введіть номер місця (1-6): ");
                    int seat = scanner.nextInt();

                    if (row < 1 || row > 5 || seat < 1 || seat > 6) {
                        System.out.println("Неправильний номер ряду або місця.");
                    } else {
                        int r = row - 1;
                        int c = seat - 1;

                        if (hall[r][c] == 'X') {
                            System.out.println("Це місце вже зайняте.");
                        } else {
                            hall[r][c] = 'X';
                            System.out.println("Місце успішно заброньовано!");
                        }
                    }
                    break;

                case 3:
                    int sold = 0;
                    int free = 0;

                    for (int i = 0; i < hall.length; i++) {
                        for (int j = 0; j < hall[i].length; j++) {

                            if (hall[i][j] == 'X') {
                                sold++;
                            } else {
                                free++;
                            }
                        }
                    }

                    int allSeats = sold + free;
                    double percent = (double) sold / allSeats * 100;
                    int money = sold * 180;

                    System.out.println();
                    System.out.println("Продано квитків: " + sold);
                    System.out.println("Вільних місць: " + free);
                    System.out.println("Заповненість залу: " + percent + "%");
                    System.out.println("Загальна виручка: " + money + " грн");
                    break;

                case 0:
                    System.out.println("Роботу завершено.");
                    break;

                default:
                    System.out.println("Такого пункту немає.");
            }
        }

        scanner.close();
    }
}
