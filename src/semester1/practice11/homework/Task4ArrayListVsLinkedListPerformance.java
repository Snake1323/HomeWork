package semester1.practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Task4ArrayListVsLinkedListPerformance {
    public static double measureInsertions(List<Integer> list, int count, boolean middle) {
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            int index = 0;
            if (middle) index = list.size() / 2;
            list.add(index, i);
        }
        return (System.nanoTime() - start) / 1_000_000.0;
    }

    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        int count = 20000;
        for (int test = 0; test < 2; test++) {
            arrayList.clear();
            linkedList.clear();
            boolean middle = test == 1;
            double arrayTime = measureInsertions(arrayList, count, middle);
            double linkedTime = measureInsertions(linkedList, count, middle);
            System.out.println(middle ? "Вставка в середину:" : "Вставка на початок:");
            System.out.println("ArrayList: " + arrayTime + " мс");
            System.out.println("LinkedList: " + linkedTime + " мс");
            if (arrayTime < linkedTime) {
                System.out.println("У цьому вимірюванні швидший ArrayList");
            } else if (linkedTime < arrayTime) {
                System.out.println("У цьому вимірюванні швидший LinkedList");
            } else {
                System.out.println("Час однаковий");
            }
        }
        List<Integer> numbers = new ArrayList<>(Arrays.asList(45, 12, 89, 3, 22, 67));
        System.out.println("До сортування: " + numbers);
        Collections.sort(numbers);
        System.out.println("Після сортування: " + numbers);
        List<Integer> sub = numbers.subList(1, 4);
        System.out.println("Підсписок: " + sub);
        sub.set(0, 999);
        System.out.println("Оригінальний список після зміни: " + numbers);
    }
}
