package semester1.practice12.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

abstract class Shape {
    private final String id;
    private final String color;

    public Shape(String id, String color) {
        this.id = Objects.requireNonNull(id);
        this.color = Objects.requireNonNull(color);
    }

    public String getId() { return id; }
    public String getColor() { return color; }
    public abstract double getArea();
    public abstract double getPerimeter();

    @Override
    public boolean equals(Object o) {
        return o instanceof Shape && id.equals(((Shape) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public String toString() { return getClass().getSimpleName() + "[" + id + ", " + color + "]"; }
}

class Circle extends Shape {
    private final double radius;

    public Circle(String id, String color, double radius) {
        super(id, color);
        if (!Double.isFinite(radius) || radius <= 0) throw new IllegalArgumentException("Радіус має бути додатним");
        this.radius = radius;
    }

    @Override
    public double getArea() { return Math.PI * radius * radius; }

    @Override
    public double getPerimeter() { return 2 * Math.PI * radius; }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(String id, String color, double width, double height) {
        super(id, color);
        if (!Double.isFinite(width) || !Double.isFinite(height) || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Сторони мають бути додатними");
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() { return width * height; }

    @Override
    public double getPerimeter() { return 2 * (width + height); }
}

class RightTriangle extends Shape {
    private final double legA;
    private final double legB;

    public RightTriangle(String id, String color, double legA, double legB) {
        super(id, color);
        if (!Double.isFinite(legA) || !Double.isFinite(legB) || legA <= 0 || legB <= 0) {
            throw new IllegalArgumentException("Катети мають бути додатними");
        }
        this.legA = legA;
        this.legB = legB;
    }

    @Override
    public double getArea() { return legA * legB / 2; }

    @Override
    public double getPerimeter() { return legA + legB + Math.hypot(legA, legB); }
}

class GeometryCanvas {
    private final String title;
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final HashSet<String> usedIds = new HashSet<>();

    public GeometryCanvas(String title) { this.title = title; }
    public String getTitle() { return title; }

    public boolean addShape(Shape s) {
        if (s == null || !usedIds.add(s.getId())) return false;
        shapes.add(s);
        return true;
    }

    public int removeSmallShapes(double minArea) {
        Iterator<Shape> it = shapes.iterator();
        int removed = 0;
        while (it.hasNext()) {
            Shape s = it.next();
            if (s.getArea() < minArea) {
                it.remove();
                usedIds.remove(s.getId());
                removed++;
                System.out.printf(Locale.US, "Видалено: %s (площа %.2f)%n", s.getId(), s.getArea());
            }
        }
        return removed;
    }

    public double getTotalArea() {
        double total = 0;
        for (Shape s : shapes) total += s.getArea();
        return total;
    }

    public double getTotalPerimeter() {
        double total = 0;
        for (Shape s : shapes) total += s.getPerimeter();
        return total;
    }

    public HashSet<String> getUniqueColors() {
        HashSet<String> colors = new HashSet<>();
        for (Shape s : shapes) colors.add(s.getColor());
        return colors;
    }

    public ArrayList<Shape> getShapesCopy() { return new ArrayList<>(shapes); }
}

public class Task3GeometryCanvasAndStats {
    public static void printStats(GeometryCanvas canvas) {
        System.out.printf(Locale.US, "Фігур: %d | Сумарна площа: %.2f | Сумарний периметр: %.2f%n",
                canvas.getShapesCopy().size(), canvas.getTotalArea(), canvas.getTotalPerimeter());
        System.out.println("Унікальні кольори: " + canvas.getUniqueColors());
    }

    public static void main(String[] args) {
        GeometryCanvas canvas1 = new GeometryCanvas("Головне полотно");
        Shape[] figures = {
                new Circle("C-1", "Red", 3), new Circle("C-1", "Blue", 5),
                new Rectangle("R-1", "Blue", 4, 5), new Rectangle("R-2", "Green", 2, 3),
                new RightTriangle("T-1", "Red", 3, 4), new RightTriangle("T-2", "Yellow", 6, 8)
        };
        System.out.println("Додавання фігур на '" + canvas1.getTitle() + "':");
        for (Shape s : figures) {
            System.out.println((canvas1.addShape(s) ? "Додано: " : "Відхилено (дублікат ID): ") + s);
        }
        System.out.println("ДО ОЧИЩЕННЯ");
        printStats(canvas1);
        assert canvas1.getShapesCopy().size() == 5;
        assert Math.abs(canvas1.getTotalArea() - (9 * Math.PI + 56)) < 0.000001;
        int removed = canvas1.removeSmallShapes(10.0);
        System.out.println("ПІСЛЯ ОЧИЩЕННЯ | Видалено: " + removed);
        printStats(canvas1);
        HashSet<String> palette1 = canvas1.getUniqueColors();
        HashSet<String> palette2 = new HashSet<>(Arrays.asList("Blue", "Yellow", "Purple"));
        HashSet<String> common = new HashSet<>(palette1);
        common.retainAll(palette2);
        HashSet<String> exclusive = new HashSet<>(palette1);
        exclusive.removeAll(palette2);
        HashSet<String> all = new HashSet<>(palette1);
        all.addAll(palette2);
        System.out.println("Палітра полотна 2: " + palette2);
        System.out.println("Спільні кольори: " + common);
        System.out.println("Тільки на полотні 1: " + exclusive);
        System.out.println("Усі кольори разом: " + all);
        assert removed == 2 && canvas1.getShapesCopy().size() == 3;
        assert Math.abs(canvas1.getTotalArea() - (9 * Math.PI + 44)) < 0.000001;
        assert Math.abs(canvas1.getTotalPerimeter() - (6 * Math.PI + 42)) < 0.000001;
    }
}
