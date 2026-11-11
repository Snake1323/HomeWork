package semester1.practice10.homework;

import java.util.Locale;

interface Scalable2D {
    double MIN_SCALE_FACTOR = 0.1;

    void scale(double factor);
}

interface Rotatable2D {
    void rotate(double deltaDegrees);

    double getRotationAngle();
}

abstract class AbstractShape2D {
    private final String name;
    private double centerX;
    private double centerY;

    public AbstractShape2D(String name, double centerX, double centerY) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    public final double getIsoperimetricQuotient() {
        return 4.0 * Math.PI * getArea() / (getPerimeter() * getPerimeter());
    }

    public String getName() {
        return name;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    @Override
    public String toString() {
        return getName() + " center=(" + getCenterX() + "; " + getCenterY() + ")";
    }
}

class CircleShape extends AbstractShape2D implements Scalable2D {
    private double radius;

    public CircleShape(String name, double centerX, double centerY, double radius) {
        super(name, centerX, centerY);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2.0 * Math.PI * radius;
    }

    @Override
    public void scale(double factor) {
        if (factor < MIN_SCALE_FACTOR) {
            factor = MIN_SCALE_FACTOR;
        }
        radius = radius * factor;
    }

    @Override
    public String toString() {
        return String.format("CircleShape['%s', center=(%s; %s), r=%.2f | S=%.2f, P=%.2f, Q=%.4f]",
                getName(), getCenterX(), getCenterY(), radius, getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}

class RectangleShape extends AbstractShape2D implements Scalable2D, Rotatable2D {
    private double width;
    private double height;
    private double rotationAngle;

    public RectangleShape(String name, double centerX, double centerY, double width, double height) {
        super(name, centerX, centerY);
        this.width = width;
        this.height = height;
        this.rotationAngle = 0.0;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2.0 * (width + height);
    }

    @Override
    public void scale(double factor) {
        if (factor < MIN_SCALE_FACTOR) {
            factor = MIN_SCALE_FACTOR;
        }
        width = width * factor;
        height = height * factor;
    }

    @Override
    public void rotate(double deltaDegrees) {
        rotationAngle = (rotationAngle + deltaDegrees) % 360.0;
        if (rotationAngle < 0) {
            rotationAngle = rotationAngle + 360.0;
        }
    }

    @Override
    public double getRotationAngle() {
        return rotationAngle;
    }

    @Override
    public String toString() {
        return String.format("RectangleShape['%s', center=(%s; %s), %.2fx%.2f, angle=%s° | S=%.2f, P=%.2f, Q=%.4f]",
                getName(), getCenterX(), getCenterY(), width, height, rotationAngle,
                getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}

final class ImmutableReferenceSquare extends AbstractShape2D {
    private final double side;

    public ImmutableReferenceSquare(String name, double centerX, double centerY, double side) {
        super(name, centerX, centerY);
        this.side = side;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public double getPerimeter() {
        return 4.0 * side;
    }

    @Override
    public String toString() {
        return String.format("ImmutableReferenceSquare['%s', center=(%s; %s), side=%.2f | S=%.2f, P=%.2f, Q=%.4f]",
                getName(), getCenterX(), getCenterY(), side, getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}

public class Task1PolymorphicShapesAndTransforms {

    public static double calculateTotalArea(AbstractShape2D[] shapes) {
        double total = 0.0;
        for (int i = 0; i < shapes.length; i++) {
            total = total + shapes[i].getArea();
        }
        return total;
    }

    public static void transformScene(AbstractShape2D[] shapes, double scaleFactor, double rotateDeg) {
        System.out.printf("-> Виконуємо пакетну трансформацію (scale x%.1f, rotate %+.1f°) через instanceof...%n",
                scaleFactor, rotateDeg);
        for (int i = 0; i < shapes.length; i++) {
            AbstractShape2D shape = shapes[i];
            if (shape instanceof Scalable2D) {
                ((Scalable2D) shape).scale(scaleFactor);
                if (shape instanceof CircleShape circle) {
                    System.out.printf("   [Scalable2D] '%s' масштабовано в %.1f разів (новий r=%.2f)%n",
                            shape.getName(), scaleFactor, circle.getRadius());
                } else if (shape instanceof RectangleShape rect) {
                    System.out.printf("   [Scalable2D] '%s' масштабовано в %.1f разів (%.2fx%.2f)%n",
                            shape.getName(), scaleFactor, rect.getWidth(), rect.getHeight());
                }
            }
            if (shape instanceof Rotatable2D) {
                ((Rotatable2D) shape).rotate(rotateDeg);
                Rotatable2D rot = (Rotatable2D) shape;
                System.out.printf("   [Rotatable2D] '%s' повернуто на %+.1f° (поточний кут: %.1f°)%n",
                        shape.getName(), rotateDeg, rot.getRotationAngle());
            }
            if (!(shape instanceof Scalable2D) && !(shape instanceof Rotatable2D)) {
                System.out.printf("   [Info] '%s' (final class) пропущено — не підтримує трансформації.%n",
                        shape.getName());
            }
        }
    }

    private static void printShapes(AbstractShape2D[] shapes) {
        for (int i = 0; i < shapes.length; i++) {
            System.out.println("  " + (i + 1) + ". " + shapes[i]);
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        AbstractShape2D[] shapes = {
                new CircleShape("Коло-Лінза", 0.0, 0.0, 5.0),
                new RectangleShape("Рамка-А", 2.0, 3.0, 4.0, 6.0),
                new ImmutableReferenceSquare("Еталон-Квадрат", 10.0, 10.0, 5.0)
        };

        System.out.println("================= ПОЛІМОРФНА ГЕОМЕТРІЯ ТА ІНТЕРФЕЙСИ ТРАНСФОРМАЦІЙ ================");
        System.out.println("[СТАН СЦЕНИ ДО ТРАНСФОРМАЦІЇ]:");
        printShapes(shapes);
        System.out.printf("Сумарна площа сцени: %.2f кв.од.%n", calculateTotalArea(shapes));
        System.out.println();

        transformScene(shapes, 2.0, 45.0);
        System.out.println();

        System.out.println("[СТАН СЦЕНИ ПІСЛЯ ТРАНСФОРМАЦІЇ]:");
        printShapes(shapes);
        System.out.printf("Нова сумарна площа сцени: %.2f кв.од.%n", calculateTotalArea(shapes));
        System.out.println("=================================================================================");
    }
}
