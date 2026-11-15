package semester1.practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Task3ListHierarchyAndSafeIterator {
    public static void demonstrateFailFastCrash(List<String> logs) {
        try {
            for (String log : logs) {
                if (log != null && log.contains("CORRUPTED")) {
                    logs.remove(log);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException: список змінено напряму, modCount != expectedModCount");
        }
    }

    public static int purgeLogsByKeywordSafely(List<String> logs, String keyword) {
        Iterator<String> it = logs.iterator();
        int removed = 0;
        while (it.hasNext()) {
            String log = it.next();
            if (log == null || log.trim().isEmpty() || log.contains(keyword)) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    public static void enrichCriticalLogsWithListIterator(List<String> logs) {
        ListIterator<String> listIt = logs.listIterator();
        while (listIt.hasNext()) {
            String trimmed = listIt.next().trim();
            listIt.set(trimmed);
            if (trimmed.startsWith("ERROR:")) {
                listIt.add("[ALERT-TRIGGERED]");
            }
        }
    }

    public static List<String> collectReverseOrder(List<String> logs) {
        List<String> reversed = new ArrayList<>();
        ListIterator<String> listIt = logs.listIterator(logs.size());
        while (listIt.hasPrevious()) {
            reversed.add(listIt.previous());
        }
        return reversed;
    }

    public static void main(String[] args) {
        List<String> rawLogs = Arrays.asList("INFO: Auth OK", "CORRUPTED_PACKET_01",
                "  ERROR: DB Timeout  ", "DEBUG: Ping", "CORRUPTED_PACKET_02",
                null, "ERROR: Disk Full", "INFO: Logout");
        System.out.println("Початкові логи: " + rawLogs);
        demonstrateFailFastCrash(new ArrayList<>(rawLogs));
        List<String> arrayLogs = new ArrayList<>(rawLogs);
        List<String> linkedLogs = new LinkedList<>(rawLogs);
        System.out.println("Видалено з ArrayList: " + purgeLogsByKeywordSafely(arrayLogs, "CORRUPTED"));
        System.out.println("Видалено з LinkedList: " + purgeLogsByKeywordSafely(linkedLogs, "CORRUPTED"));
        enrichCriticalLogsWithListIterator(arrayLogs);
        enrichCriticalLogsWithListIterator(linkedLogs);
        System.out.println("ArrayList: " + arrayLogs);
        System.out.println("LinkedList: " + linkedLogs);
        System.out.println("Результати однакові: " + arrayLogs.equals(linkedLogs));
        System.out.println("ArrayList у зворотному порядку: " + collectReverseOrder(arrayLogs));
        System.out.println("LinkedList у зворотному порядку: " + collectReverseOrder(linkedLogs));
    }
}
