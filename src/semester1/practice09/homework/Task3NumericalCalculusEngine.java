package semester1.practice09.homework;

abstract class AbstractFunction1D {
    private final String expressionName;

    public AbstractFunction1D(String expressionName) {
        this.expressionName = expressionName;
    }

    public String getExpressionName() {
        return expressionName;
    }

    public abstract double evaluate(double x);

    public abstract double analyticDerivative(double x);

    public final double numericalDerivative(double x) {
        double h = 1e-5;
        return (evaluate(x + h) - evaluate(x - h)) / (2 * h);
    }

    public final double getDerivativeError(double x) {
        return Math.abs(analyticDerivative(x) - numericalDerivative(x));
    }

    public final double integrateTrapezoidal(double a, double b, int intervals) {
        if (intervals <= 0) {
            intervals = 1000;
        }
        double dx = (b - a) / intervals;
        double sum = (evaluate(a) + evaluate(b)) / 2.0;
        for (int i = 1; i < intervals; i++) {
            sum = sum + evaluate(a + i * dx);
        }
        return sum * dx;
    }

    @Override
    public String toString() {
        return "Функція " + expressionName;
    }
}

class PolynomialFunction extends AbstractFunction1D {
    private final double[] coeffs;

    public PolynomialFunction(String expressionName, double[] coeffs) {
        super(expressionName);
        if (coeffs == null) {
            this.coeffs = new double[0];
        } else {
            this.coeffs = new double[coeffs.length];
            for (int i = 0; i < coeffs.length; i++) {
                this.coeffs[i] = coeffs[i];
            }
        }
    }

    public double[] getCoefficients() {
        double[] copy = new double[coeffs.length];
        for (int i = 0; i < coeffs.length; i++) {
            copy[i] = coeffs[i];
        }
        return copy;
    }

    @Override
    public double evaluate(double x) {
        double sum = 0.0;
        for (int i = 0; i < coeffs.length; i++) {
            sum = sum + coeffs[i] * Math.pow(x, i);
        }
        return sum;
    }

    @Override
    public double analyticDerivative(double x) {
        double sum = 0.0;
        for (int i = 1; i < coeffs.length; i++) {
            sum = sum + i * coeffs[i] * Math.pow(x, i - 1);
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Многочлен ");
        sb.append(getExpressionName()).append(" = ");
        for (int i = 0; i < coeffs.length; i++) {
            if (i > 0) {
                sb.append(" + ");
            }
            sb.append(String.format(java.util.Locale.US, "%.2f", coeffs[i]));
            if (i == 1) {
                sb.append("*x");
            } else if (i > 1) {
                sb.append("*x^").append(i);
            }
        }
        return sb.toString();
    }
}

class SineWaveFunction extends AbstractFunction1D {
    private final double amplitude;
    private final double omega;
    private final double phase;

    public SineWaveFunction(String expressionName, double amplitude, double omega, double phase) {
        super(expressionName);
        this.amplitude = amplitude;
        this.omega = omega;
        this.phase = phase;
    }

    @Override
    public double evaluate(double x) {
        return amplitude * Math.sin(omega * x + phase);
    }

    @Override
    public double analyticDerivative(double x) {
        return amplitude * omega * Math.cos(omega * x + phase);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "Гармонічна функція %s = %.2f*sin(%.2f*x + %.2f)",
                getExpressionName(), amplitude, omega, phase);
    }
}

class CompositeFunction1D extends AbstractFunction1D {
    private final AbstractFunction1D outer;
    private final AbstractFunction1D inner;

    public CompositeFunction1D(AbstractFunction1D outer, AbstractFunction1D inner) {
        super("(" + outer.getExpressionName() + " ∘ " + inner.getExpressionName() + ")");
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public double evaluate(double x) {
        return outer.evaluate(inner.evaluate(x));
    }

    @Override
    public double analyticDerivative(double x) {
        return outer.analyticDerivative(inner.evaluate(x)) * inner.analyticDerivative(x);
    }

    @Override
    public String toString() {
        return "Суперпозиція " + getExpressionName();
    }
}

public class Task3NumericalCalculusEngine {
    public static void main(String[] args) {
        System.out.println("================ ЛАБОРАТОРІЯ ЧИСЕЛЬНОГО АНАЛІЗУ ================");

        PolynomialFunction g = new PolynomialFunction("g(x)", new double[]{1.0, 0.0, 1.0});
        System.out.println("1. " + g);
        System.out.printf(java.util.Locale.US, "   Значення g(2.0) = %.2f%n", g.evaluate(2.0));
        System.out.printf(java.util.Locale.US, "   Аналітична похідна g'(2.0) = %.6f | Чисельна = %.6f (Похибка: %.2e)%n",
                g.analyticDerivative(2.0), g.numericalDerivative(2.0), g.getDerivativeError(2.0));
        System.out.printf(java.util.Locale.US, "   Інтеграл на [%.2f; %.2f] (метод трапецій, N=1000): %.4f (Точне значення: %.1f)%n",
                0.0, 3.0, g.integrateTrapezoidal(0.0, 3.0, 1000), 12.0);
        System.out.println();

        SineWaveFunction f = new SineWaveFunction("f(x)", 2.0, 1.0, 0.0);
        System.out.println("2. " + f);
        System.out.printf(java.util.Locale.US, "   Інтеграл на [%.2f; %.4f] (N=1000): %.4f%n",
                0.0, 3.1416, f.integrateTrapezoidal(0.0, 3.1416, 1000));
        System.out.println();

        CompositeFunction1D h = new CompositeFunction1D(f, g);
        System.out.println("3. Суперпозиція (Композиція об'єктів) H(x) = f(g(x)) = 2*sin(x^2 + 1):");
        System.out.println("   Точка x = 1.50:");
        System.out.printf(java.util.Locale.US, "   -> H(1.50) = 2 * sin(%.2f) = %.6f%n", g.evaluate(1.5), h.evaluate(1.5));
        System.out.printf(java.util.Locale.US, "   -> Аналітична похідна H'(1.50) за правилом ланцюга: %.6f%n", h.analyticDerivative(1.5));
        System.out.printf(java.util.Locale.US, "   -> Чисельна похідна H'(1.50) через кінцеву різницю: %.6f%n", h.numericalDerivative(1.5));
        System.out.printf(java.util.Locale.US, "   -> Розбіжність між формулами: %.2e (ІДЕАЛЬНИЙ ЗБІГ!)%n", h.getDerivativeError(1.5));

        System.out.println("================================================================");
    }
}
