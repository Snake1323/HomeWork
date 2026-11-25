package semester1.practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

abstract class Payment {
    private final String id;
    private final String shopName;
    private final double amount;

    public Payment(String id, String shopName, double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Сума має бути додатною");
        }
        this.id = Objects.requireNonNull(id);
        this.shopName = Objects.requireNonNull(shopName);
        this.amount = amount;
    }

    public String getId() { return id; }
    public String getShopName() { return shopName; }
    public double getAmount() { return amount; }
    public abstract double calculateFee();
    public double getNetAmount() { return amount - calculateFee(); }

    @Override
    public boolean equals(Object o) {
        return o instanceof Payment && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}

class CardPayment extends Payment {
    public CardPayment(String id, String shopName, double amount) { super(id, shopName, amount); }

    @Override
    public double calculateFee() { return getAmount() * 0.015; }
}

class TransferPayment extends Payment {
    public TransferPayment(String id, String shopName, double amount) { super(id, shopName, amount); }

    @Override
    public double calculateFee() { return getAmount() * 0.02 + 20.0; }
}

class PaymentProcessor {
    private final ArrayList<Payment> payments = new ArrayList<>();
    private final HashSet<String> blockedShops;
    private final double maxLimit;

    public PaymentProcessor(HashSet<String> blockedShops, double maxLimit) {
        if (!Double.isFinite(maxLimit) || maxLimit <= 0) {
            throw new IllegalArgumentException("Ліміт має бути додатним");
        }
        this.blockedShops = new HashSet<>(blockedShops);
        this.maxLimit = maxLimit;
    }

    public void addPayment(Payment p) { payments.add(Objects.requireNonNull(p)); }
    public HashSet<String> getBlockedShopsCopy() { return new HashSet<>(blockedShops); }
    public ArrayList<Payment> getPaymentsCopy() { return new ArrayList<>(payments); }

    public int cleanPayments() {
        HashSet<String> seenIds = new HashSet<>();
        Iterator<Payment> it = payments.iterator();
        int removed = 0;
        while (it.hasNext()) {
            Payment p = it.next();
            String reason;
            if (!seenIds.add(p.getId())) reason = "ДУБЛІКАТ";
            else if (blockedShops.contains(p.getShopName())) reason = "ЧОРНИЙ СПИСОК";
            else if (p.getAmount() > maxLimit) reason = "ПОНАД ЛІМІТ";
            else continue;
            System.out.printf(Locale.US, "[%s] %s | %s (%.2f грн) — видалено!%n",
                    reason, p.getId(), p.getShopName(), p.getAmount());
            it.remove();
            removed++;
        }
        return removed;
    }

    public double getTotalGross() {
        double total = 0;
        for (Payment p : payments) total += p.getAmount();
        return total;
    }

    public double getTotalFees() {
        double total = 0;
        for (Payment p : payments) total += p.calculateFee();
        return total;
    }

    public HashSet<String> getApprovedShops() {
        HashSet<String> shops = new HashSet<>();
        for (Payment p : payments) shops.add(p.getShopName());
        return shops;
    }
}

public class Task2PaymentFilterAndAudit {
    public static void main(String[] args) {
        HashSet<String> blocked = new HashSet<>(Arrays.asList("ScamShop", "FakeStore"));
        PaymentProcessor processor = new PaymentProcessor(blocked, 20000.0);
        processor.addPayment(new CardPayment("P-01", "Rozetka", 4000));
        processor.addPayment(new TransferPayment("P-02", "Comfy", 10000));
        processor.addPayment(new CardPayment("P-01", "Rozetka", 4000));
        processor.addPayment(new CardPayment("P-03", "ScamShop", 5000));
        processor.addPayment(new TransferPayment("P-04", "LuxWatch", 25000));
        processor.addPayment(new CardPayment("P-05", "Rozetka", 2000));
        processor.addPayment(new TransferPayment("P-06", "Allo", 5000));
        System.out.println("БАНКІВСЬКИЙ ПЛАТІЖНИЙ ФІЛЬТР");
        System.out.println("Чорний список: " + processor.getBlockedShopsCopy());
        System.out.println("Ліміт: 20000.00 грн | Вхідних платежів: " + processor.getPaymentsCopy().size());
        int removed = processor.cleanPayments();
        System.out.println("Схвалені платежі:");
        for (Payment p : processor.getPaymentsCopy()) {
            System.out.printf(Locale.US, "%s | %s | Сума: %.2f | Комісія: %.2f | Нетто: %.2f грн%n",
                    p.getId(), p.getShopName(), p.getAmount(), p.calculateFee(), p.getNetAmount());
        }
        System.out.println("Видалено: " + removed + " | Схвалено: " + processor.getPaymentsCopy().size());
        System.out.printf(Locale.US, "Брутто: %.2f грн | Комісія: %.2f грн | Нетто: %.2f грн%n",
                processor.getTotalGross(), processor.getTotalFees(), processor.getTotalGross() - processor.getTotalFees());
        System.out.println("Унікальні магазини: " + processor.getApprovedShops());
        assert removed == 3 && processor.getPaymentsCopy().size() == 4;
        assert Math.abs(processor.getTotalGross() - 21000) < 0.000001;
        assert Math.abs(processor.getTotalFees() - 430) < 0.000001;
    }
}
