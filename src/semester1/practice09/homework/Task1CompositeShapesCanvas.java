package semester1.practice09.homework;

class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void translate(double dx, double dy) {
        this.x = this.x + dx;
        this.y = this.y + dy;
    }

    public double distanceTo(Point2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "(%.2f; %.2f)", x, y);
    }
}

abstract class AbstractShape2D {
    private final String name;
    private final Point2D anchorPoint;

    public AbstractShape2D(String name, Point2D anchorPoint) {
        this.name = name;
        this.anchorPoint = (anchorPoint != null) ? new Point2D(anchorPoint) : new Point2D(0, 0);
    }

    public String getName() {
        return name;
    }

    public Point2D getAnchorPoint() {
        return new Point2D(anchorPoint);
    }

    public void moveBy(double dx, double dy) {
        anchorPoint.translate(dx, dy);
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "%s[name='%s', center=%s, S=%.2f, P=%.2f]",
                getClass().getSimpleName(), name, anchorPoint, getArea(), getPerimeter());
    }
}

class Circle2D extends AbstractShape2D {
    private double radius;

    public Circle2D(String name, Point2D center, double radius) {
        super(name, center);
        if (radius <= 0) {
            this.radius = 1.0;
        } else {
            this.radius = radius;
        }
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "Circle2D[name='%s', center=%s, r=%.2f | S=%.2f, P=%.2f]",
                getName(), getAnchorPoint(), radius, getArea(), getPerimeter());
    }
}

class Rectangle2D extends AbstractShape2D {
    private double width;
    private double height;

    public Rectangle2D(String name, Point2D center, double width, double height) {
        super(name, center);
        this.width = (width <= 0) ? 1.0 : width;
        this.height = (height <= 0) ? 1.0 : height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "Rectangle2D[name='%s', center=%s, %.2fx%.2f | S=%.2f, P=%.2f]",
                getName(), getAnchorPoint(), width, height, getArea(), getPerimeter());
    }
}

class RightTriangle2D extends AbstractShape2D {
    private double legA;
    private double legB;

    public RightTriangle2D(String name, Point2D center, double legA, double legB) {
        super(name, center);
        this.legA = (legA <= 0) ? 1.0 : legA;
        this.legB = (legB <= 0) ? 1.0 : legB;
    }

    public double getHypotenuse() {
        return Math.sqrt(legA * legA + legB * legB);
    }

    @Override
    public double getArea() {
        return legA * legB / 2.0;
    }

    @Override
    public double getPerimeter() {
        return legA + legB + getHypotenuse();
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "RightTriangle2D[name='%s', center=%s, a=%.2f, b=%.2f, c=%.2f | S=%.2f, P=%.2f]",
                getName(), getAnchorPoint(), legA, legB, getHypotenuse(), getArea(), getPerimeter());
    }
}

class GeometryCanvas {
    private final String title;
    private final AbstractShape2D[] shapes;
    private int count;

    public GeometryCanvas(String title, int capacity) {
        this.title = title;
        this.shapes = new AbstractShape2D[Math.max(1, capacity)];
        this.count = 0;
    }

    public boolean addShape(AbstractShape2D shape) {
        if (shape == null) {
            System.out.println("Помилка: неможливо додати порожню фігуру!");
            return false;
        }
        if (count >= shapes.length) {
            System.out.println("Помилка: полотно переповнене!");
            return false;
        }
        shapes[count] = shape;
        count++;
        return true;
    }

    public double getTotalArea() {
        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum = sum + shapes[i].getArea();
        }
        return sum;
    }

    public double getTotalPerimeter() {
        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum = sum + shapes[i].getPerimeter();
        }
        return sum;
    }

    public AbstractShape2D findLargestShape() {
        if (count == 0) {
            return null;
        }
        AbstractShape2D largest = shapes[0];
        for (int i = 1; i < count; i++) {
            if (shapes[i].getArea() > largest.getArea()) {
                largest = shapes[i];
            }
        }
        return largest;
    }

    public void moveAllShapes(double dx, double dy) {
        for (int i = 0; i < count; i++) {
            shapes[i].moveBy(dx, dy);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Полотно: '").append(title).append("' (Фігур: ")
                .append(count).append("/").append(shapes.length).append(")\n");
        for (int i = 0; i < count; i++) {
            sb.append("  ").append(i + 1).append(". ").append(shapes[i]).append("\n");
        }
        sb.append("-----------------------------------------------------------------------------\n");
        sb.append(String.format(java.util.Locale.US, "%-28s%.2f кв.од.%n", "Сумарна площа креслення:", getTotalArea()));
        sb.append(String.format(java.util.Locale.US, "%-28s%.2f од.%n", "Сумарний периметр:", getTotalPerimeter()));
        AbstractShape2D largest = findLargestShape();
        if (largest != null) {
            sb.append(String.format(java.util.Locale.US, "%-28s%s (S = %.2f)", "Найбільша фігура:", largest.getName(), largest.getArea()));
        }
        return sb.toString();
    }
}

public class Task1CompositeShapesCanvas {
    public static void main(String[] args) {
        System.out.println("================ ГЕОМЕТРИЧНЕ ПОЛОТНО (КОМПОЗИЦІЯ + ABSTRACT) ================");

        GeometryCanvas canvas = new GeometryCanvas("Інженерне креслення №1", 5);

        Point2D shared = new Point2D(0, 0);
        Circle2D circle = new Circle2D("Коло-А", shared, 5.0);
        shared.translate(100, 100);
        canvas.addShape(circle);

        canvas.addShape(new Rectangle2D("Плита-Б", new Point2D(10, 5), 4.0, 6.0));
        canvas.addShape(new RightTriangle2D("Клин-В", new Point2D(-3, 2), 3.0, 4.0));

        System.out.println(canvas);

        System.out.println("-> Переміщуємо всі фігури полотна на вектор (+5.0; -2.0)...");
        canvas.moveAllShapes(5.0, -2.0);
        System.out.println("Новий центр 'Коло-А': " + circle.getAnchorPoint());

        System.out.println("============================================================================");
    }
}
