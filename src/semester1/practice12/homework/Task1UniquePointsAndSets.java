package semester1.practice12.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

class Point2D {
    private final int x;
    private final int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public double distanceFromOrigin() { return Math.hypot(x, y); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2D)) return false;
        Point2D point = (Point2D) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() { return Objects.hash(x, y); }

    @Override
    public String toString() { return "(" + x + "; " + y + ")"; }
}

class PointSetUtils {
    public static HashSet<Point2D> union(HashSet<Point2D> a, HashSet<Point2D> b) {
        HashSet<Point2D> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    public static HashSet<Point2D> intersection(HashSet<Point2D> a, HashSet<Point2D> b) {
        HashSet<Point2D> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    public static HashSet<Point2D> difference(HashSet<Point2D> a, HashSet<Point2D> b) {
        HashSet<Point2D> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    public static int removeFarPoints(HashSet<Point2D> points, double maxRadius) {
        Iterator<Point2D> it = points.iterator();
        int removed = 0;
        while (it.hasNext()) {
            if (it.next().distanceFromOrigin() > maxRadius) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }
}

public class Task1UniquePointsAndSets {
    public static void main(String[] args) {
        ArrayList<Point2D> sensorA = new ArrayList<>(Arrays.asList(
                new Point2D(0, 0), new Point2D(3, 4), new Point2D(1, 2),
                new Point2D(3, 4), new Point2D(6, 8), new Point2D(5, 0), new Point2D(1, 2)));
        ArrayList<Point2D> sensorB = new ArrayList<>(Arrays.asList(
                new Point2D(3, 4), new Point2D(5, 0), new Point2D(2, 2),
                new Point2D(8, 6), new Point2D(2, 2), new Point2D(0, 10)));
        HashSet<Point2D> setA = new HashSet<>(sensorA);
        HashSet<Point2D> setB = new HashSet<>(sensorB);
        System.out.println("ГЕОДЕЗИЧНИЙ РАДАР");
        System.out.println("Сенсор A: отримано " + sensorA.size() + ", унікальних: " + setA.size() + " " + setA);
        System.out.println("Сенсор B: отримано " + sensorB.size() + ", унікальних: " + setB.size() + " " + setB);
        HashSet<Point2D> union = PointSetUtils.union(setA, setB);
        HashSet<Point2D> intersection = PointSetUtils.intersection(setA, setB);
        HashSet<Point2D> difference = PointSetUtils.difference(setA, setB);
        System.out.println("Об'єднання [" + union.size() + "]: " + union);
        System.out.println("Перетин [" + intersection.size() + "]: " + intersection);
        System.out.println("Різниця A - B [" + difference.size() + "]: " + difference);
        assert setA.size() == 5 && setB.size() == 5;
        assert union.size() == 8 && intersection.size() == 2 && difference.size() == 3;
        int removed = PointSetUtils.removeFarPoints(union, 7.0);
        System.out.println("Видалено точок поза радіусом 7.0: " + removed);
        System.out.println("Залишилось у зоні [" + union.size() + "]: " + union);
        assert removed == 3 && union.size() == 5;
    }
}
