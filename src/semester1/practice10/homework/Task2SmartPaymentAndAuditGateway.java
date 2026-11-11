package semester1.practice10.homework;

import java.util.Locale;

interface CashbackCapable {
    double MAX_CASHBACK_UAH = 500.0;

    double getCashbackRate();

    default double calculateCashback(double baseAmount) {
        return Math.min(MAX_CASHBACK_UAH, baseAmount * getCashbackRate());
    }
}

interface Refundable {
    boolean requestRefund(String reason);

    boolean isRefunded();
}

interface BlockchainVerifiable {
    String getTxHash();

    int getConfirmations();

    default boolean isFinalized() {
        return getConfirmations() >= 12;
    }
}

abstract class AbstractPaymentTransaction {
    private final String txId;
    private final double baseAmountUah;
    private boolean processed;

    public AbstractPaymentTransaction(String txId, double baseAmountUah) {
        this.txId = txId;
        this.baseAmountUah = baseAmountUah;
    }

    public abstract double calculateFee();

    public abstract String getPaymentChannel();

    public final boolean process() {
        if (baseAmountUah <= 0 || processed) {
            return false;
        }
        processed = true;
        return true;
    }

    public final double getTotalCharged() {
        return baseAmountUah + calculateFee();
    }

    public String getTxId() {
        return txId;
    }

    public double getBaseAmount() {
        return baseAmountUah;
    }

    public boolean isProcessed() {
        return processed;
    }
}

class PremiumCardPayment extends AbstractPaymentTransaction implements CashbackCapable, Refundable {
    private final String maskedPan;
    private final double cashbackRate;
    private boolean refunded;

    public PremiumCardPayment(String txId, double amount, String maskedPan, double cashbackRate) {
        super(txId, amount);
        this.maskedPan = maskedPan;
        this.cashbackRate = cashbackRate;
    }

    @Override
    public double calculateFee() {
        return getBaseAmount() * 0.01;
    }

    @Override
    public String getPaymentChannel() {
        return "Visa Infinite";
    }

    @Override
    public double getCashbackRate() {
        return cashbackRate;
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }

    public String getMaskedPan() {
        return maskedPan;
    }
}

class CryptoUsdtPayment extends AbstractPaymentTransaction implements CashbackCapable, BlockchainVerifiable {
    private final String txHash;
    private final int confirmations;

    public CryptoUsdtPayment(String txId, double amount, String txHash, int confirmations) {
        super(txId, amount);
        this.txHash = txHash;
        this.confirmations = confirmations;
    }

    @Override
    public double calculateFee() {
        return 40.0;
    }

    @Override
    public String getPaymentChannel() {
        return "USDT TRC-20";
    }

    @Override
    public double getCashbackRate() {
        return 0.01;
    }

    @Override
    public String getTxHash() {
        return txHash;
    }

    @Override
    public int getConfirmations() {
        return confirmations;
    }
}

final class TaxTreasuryPayment extends AbstractPaymentTransaction implements Refundable {
    private final String taxCode;
    private boolean refunded;

    public TaxTreasuryPayment(String txId, double amount, String taxCode) {
        super(txId, amount);
        this.taxCode = taxCode;
    }

    @Override
    public double calculateFee() {
        return 0.0;
    }

    @Override
    public String getPaymentChannel() {
        return "ДержКазначейство";
    }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }

    public String getTaxCode() {
        return taxCode;
    }
}

public class Task2SmartPaymentAndAuditGateway {

    public static void auditLedger(AbstractPaymentTransaction[] ledger) {
        System.out.println("[1. ПРОВЕДЕННЯ ПАКЕТУ ТРАНЗАКЦІЙ (final process())]:");
        double totalAmount = 0.0;
        double totalFees = 0.0;
        for (int i = 0; i < ledger.length; i++) {
            AbstractPaymentTransaction tx = ledger[i];
            boolean ok = tx.process();
            totalAmount = totalAmount + tx.getBaseAmount();
            totalFees = totalFees + tx.calculateFee();
            System.out.printf("  %s [%s | %s] Сума: %.2f грн | Комісія: %.2f грн | Всього: %.2f грн%n",
                    ok ? "✅" : "❌", tx.getTxId(), tx.getPaymentChannel(),
                    tx.getBaseAmount(), tx.calculateFee(), tx.getTotalCharged());
        }

        System.out.println();
        System.out.println("[2. АВТОМАТИЧНИЙ АУДИТ МОЖЛИВОСТЕЙ (Pattern Matching instanceof)]:");

        double totalCashback = 0.0;
        for (int i = 0; i < ledger.length; i++) {
            AbstractPaymentTransaction tx = ledger[i];
            if (tx instanceof CashbackCapable cash) {
                double paid = cash.calculateCashback(tx.getBaseAmount());
                totalCashback = totalCashback + paid;
                if (paid >= CashbackCapable.MAX_CASHBACK_UAH) {
                    System.out.printf("  💰 [CashbackCapable] %s: розрахунковий %.0f%% = %.0f грн -> нараховано %.2f грн (Ліміт MAX_CASHBACK_UAH!)%n",
                            tx.getTxId(), cash.getCashbackRate() * 100,
                            tx.getBaseAmount() * cash.getCashbackRate(), paid);
                } else {
                    System.out.printf("  💰 [CashbackCapable] %s: нараховано %.0f%% крипто-бонусу = %.2f грн%n",
                            tx.getTxId(), cash.getCashbackRate() * 100, paid);
                }
            }
        }

        for (int i = 0; i < ledger.length; i++) {
            AbstractPaymentTransaction tx = ledger[i];
            if (tx instanceof BlockchainVerifiable chain) {
                System.out.printf("  ⛓️ [BlockchainVerifiable] %s: Hash=%s, Підтверджень=%d (Finalized: %b)%n",
                        tx.getTxId(), chain.getTxHash(), chain.getConfirmations(), chain.isFinalized());
            }
        }

        boolean refundDone = false;
        for (int i = 0; i < ledger.length && !refundDone; i++) {
            AbstractPaymentTransaction tx = ledger[i];
            if (tx instanceof Refundable ref) {
                refundDone = true;
                boolean ok = ref.requestRefund("Подвійне списання");
                System.out.printf("  🔄 [Refundable] Спроба повернення %s ('Подвійне списання'): %s%n",
                        tx.getTxId(), ok ? "УСПІШНО ПОВЕРНЕНО!" : "ВІДМОВЛЕНО!");
            }
        }

        System.out.println();
        System.out.println("[ПІДСУМКОВИЙ ЗВІТ ШЛЮЗУ]:");
        System.out.printf("  %-26s%8.2f грн%n", "Сумарний базовий оборот:", totalAmount);
        System.out.printf("  %-26s%8.2f грн%n", "Сумарна комісія шлюзу:", totalFees);
        System.out.printf("  %-26s%8.2f грн%n", "Сумарний виплачений кешбек:", totalCashback);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        System.out.println("================ ПРОЦЕСИНГОВИЙ ЦЕНТР FINTECH (POLYMORPHISM & INTERFACES) ================");

        AbstractPaymentTransaction[] ledger = {
                new PremiumCardPayment("TX-101", 20000.0, "4149-****-****-7788", 0.03),
                new CryptoUsdtPayment("TX-102", 8500.0, "0x9f8a...c4", 18),
                new TaxTreasuryPayment("TX-103", 5000.0, "ЄДРПОУ-20004567")
        };

        auditLedger(ledger);

        System.out.println("========================================================================================");
    }
}
