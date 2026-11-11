package semester1.practice10.homework;

import java.lang.reflect.Modifier;
import java.util.Locale;

interface Evaluable1D {
    double EPSILON = 1e-7;

    double evaluate(double x);
}

interface Integrable1D extends Evaluable1D {

    default double integrateSimpson(double a, double b, int n) {
        if (n < 2) {
            n = 100;
        }
        if (n % 2 != 0) {
            n = n + 1;
        }
        double h = (b - a) / n;
        double sum = evaluate(a) + evaluate(b);
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            if (i % 2 == 1) {
                sum = sum + 4.0 * evaluate(x);
            } else {
                sum = sum + 2.0 * evaluate(x);
            }
        }
        return sum * h / 3.0;
    }
}

interface Differentiable1D extends Evaluable1D {

    double derivative(double x);

    default double numericalDerivative(double x) {
        double h = 1e-5;
        return (evaluate(x + h) - evaluate(x - h)) / (2.0 * h);
    }
}

interface NewtonRootFindable extends Differentiable1D {

    default double findRootNewton(double x0, int maxIter) {
        double x = x0;
        for (int i = 0; i < maxIter; i++) {
            if (Math.abs(evaluate(x)) <= EPSILON) {
                break;
            }
            double d = derivative(x);
            if (Math.abs(d) < 1e-12) {
                break;
            }
            x = x - evaluate(x) / d;
        }
        return x;
    }
}

abstract class AbstractMathFunction implements Integrable1D {
    private final String formulaTitle;

    public AbstractMathFunction(String formulaTitle) {
        this.formulaTitle = formulaTitle;
    }

    public final String getFormulaTitle() {
        return formulaTitle;
    }
}

final class PolynomialFunction extends AbstractMathFunction implements NewtonRootFindable {
    private final double[] coeffs;

    public PolynomialFunction(double[] coeffs) {
        super(buildTitle(coeffs));
        this.coeffs = coeffs.clone();
    }

    private static String buildTitle(double[] coeffs) {
        String title = String.format("P(x) = %.2f", coeffs[0]);
        for (int i = 1; i < coeffs.length; i++) {
            String power = (i == 1) ? "x" : "x^" + i;
            String sign = (coeffs[i] < 0) ? "-" : "+";
            title = title + " " + sign + " " + String.format("%.2f", Math.abs(coeffs[i])) + "*" + power;
        }
        return title;
    }

    @Override
    public double evaluate(double x) {
        double result = 0.0;
        for (int i = coeffs.length - 1; i >= 0; i--) {
            result = result * x + coeffs[i];
        }
        return result;
    }

    @Override
    public double derivative(double x) {
        double result = 0.0;
        for (int i = coeffs.length - 1; i >= 1; i--) {
            result = result * x + coeffs[i] * i;
        }
        return result;
    }

    public double integrateExact(double a, double b) {
        double value = 0.0;
        for (int i = 0; i < coeffs.length; i++) {
            double powA = Math.pow(a, i + 1);
            double powB = Math.pow(b, i + 1);
            value = value + coeffs[i] * (powB - powA) / (i + 1);
        }
        return value;
    }
}

class DampedHarmonyFunction extends AbstractMathFunction implements NewtonRootFindable {
    private double amplitude;
    private double decayRate;
    private double omega;

    public DampedHarmonyFunction(double amplitude, double decayRate, double omega) {
        super(String.format("D(x) = %.2f * exp(-%.2f*x) * cos(%.2f*x)", amplitude, decayRate, omega));
        this.amplitude = amplitude;
        this.decayRate = decayRate;
        this.omega = omega;
    }

    @Override
    public double evaluate(double x) {
        return amplitude * Math.exp(-decayRate * x) * Math.cos(omega * x);
    }

    @Override
    public double derivative(double x) {
        return -amplitude * Math.exp(-decayRate * x)
                * (decayRate * Math.cos(omega * x) + omega * Math.sin(omega * x));
    }
}

class StepHeavisideFunction extends AbstractMathFunction {
    private double threshold;
    private double lowValue;
    private double highValue;

    public StepHeavisideFunction(double threshold, double lowValue, double highValue) {
        super(String.format("Step(x) = {%.1f при x < %s; %.1f при x >= %s}",
                lowValue, threshold, highValue, threshold));
        this.threshold = threshold;
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    @Override
    public double evaluate(double x) {
        if (x < threshold) {
            return lowValue;
        }
        return highValue;
    }
}

public class Task3NumericalCalculusAndRootPolymorphism {

    private static String getSymbol(AbstractMathFunction fn) {
        String title = fn.getFormulaTitle();
        return title.substring(0, title.indexOf('('));
    }

    private static String getFinalMark(AbstractMathFunction fn) {
        if (Modifier.isFinal(fn.getClass().getModifiers())) {
            return "final ";
        }
        return "";
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        double a = 0.0;
        double b = 3.0;

        AbstractMathFunction[] functions = {
                new PolynomialFunction(new double[]{-5.0, -2.0, 0.0, 1.0}),
                new DampedHarmonyFunction(4.0, 0.5, 2.0),
                new StepHeavisideFunction(1.5, 2.0, 5.0)
        };

        System.out.println("================ ЧИСЕЛЬНИЙ АНАЛІЗ: ІНТЕРФЕЙСИ ТА ПОЛІМОРФІЗМ ================");
        System.out.printf("Дослідження поліморфного масиву AbstractMathFunction[%d] на відрізку [%.2f; %.2f]:%n",
                functions.length, a, b);
        System.out.println();

        for (int i = 0; i < functions.length; i++) {
            AbstractMathFunction fn = functions[i];
            int n = (fn instanceof StepHeavisideFunction) ? 1000 : 100;

            System.out.println((i + 1) + ". Функція: " + fn.getFormulaTitle()
                    + " [" + getFinalMark(fn) + fn.getClass().getSimpleName() + "]");

            System.out.printf("   -> Інтеграл Сімпсона на [%.1f; %.1f] (N=%d): %.6f",
                    a, b, n, fn.integrateSimpson(a, b, n));
            if (fn instanceof PolynomialFunction poly) {
                System.out.printf(" (Точне значення: %.6f)", poly.integrateExact(a, b));
            }
            System.out.println();

            if (fn instanceof Differentiable1D diff) {
                double x = (fn instanceof PolynomialFunction) ? 2.0 : 1.0;
                System.out.printf("   -> [Differentiable1D] Похідна %s'(%.1f): аналітична = %.6f | чисельна = %.6f%n",
                        getSymbol(fn), x, diff.derivative(x), diff.numericalDerivative(x));
            }

            if (fn instanceof NewtonRootFindable rootFn) {
                double x0 = (fn instanceof PolynomialFunction) ? 2.0 : 0.7;
                double root = rootFn.findRootNewton(x0, 50);
                System.out.printf("   -> [NewtonRootFindable] Корінь %s(x) = 0 (старт x0=%.1f): x* = %.7f",
                        getSymbol(fn), x0, root);
                if (fn instanceof PolynomialFunction poly) {
                    System.out.printf(" (Перевірка %s(x*) = %.2e)", getSymbol(fn), Math.abs(poly.evaluate(root)));
                } else {
                    System.out.printf(" (pi/4 = %.7f)", Math.PI / 4);
                }
                System.out.println();
            }

            if (!(fn instanceof Differentiable1D)) {
                System.out.println("   -> [Info] Функція розривна (не реалізує Differentiable1D / NewtonRootFindable) — пошук дотичних пропущено.");
            }

            if (i < functions.length - 1) {
                System.out.println();
            }
        }

        System.out.println("==============================================================================");
    }
}
