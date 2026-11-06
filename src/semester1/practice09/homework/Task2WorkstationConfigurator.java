package semester1.practice09.homework;

class Motherboard {
    private final String model;
    private final String socketType;
    private final int maxExpansionSlots;

    public Motherboard(String model, String socketType, int maxExpansionSlots) {
        this.model = model;
        this.socketType = socketType;
        if (maxExpansionSlots < 1) {
            this.maxExpansionSlots = 1;
        } else if (maxExpansionSlots > 8) {
            this.maxExpansionSlots = 8;
        } else {
            this.maxExpansionSlots = maxExpansionSlots;
        }
    }

    public String getModel() {
        return model;
    }

    public String getSocketType() {
        return socketType;
    }

    public int getMaxExpansionSlots() {
        return maxExpansionSlots;
    }

    public boolean isSocketCompatible(String cpuSocket) {
        return socketType.equalsIgnoreCase(cpuSocket);
    }

    @Override
    public String toString() {
        return String.format("Motherboard[model='%s', socket='%s', slots=%d]", model, socketType, maxExpansionSlots);
    }
}

class PowerSupplyUnit {
    private final String model;
    private final int maxOutputWatts;
    private final double efficiencyCoefficient;

    public PowerSupplyUnit(String model, int maxOutputWatts, double efficiencyCoefficient) {
        this.model = model;
        this.maxOutputWatts = maxOutputWatts;
        if (efficiencyCoefficient < 0.80) {
            this.efficiencyCoefficient = 0.80;
        } else if (efficiencyCoefficient > 0.96) {
            this.efficiencyCoefficient = 0.96;
        } else {
            this.efficiencyCoefficient = efficiencyCoefficient;
        }
    }

    public String getModel() {
        return model;
    }

    public int getMaxOutputWatts() {
        return maxOutputWatts;
    }

    public double getEfficiencyCoefficient() {
        return efficiencyCoefficient;
    }

    public int getSafeContinuousWatts() {
        return (int) (maxOutputWatts * 0.85);
    }

    public double calculateWallPowerDraw(int internalLoadWatts) {
        return internalLoadWatts / efficiencyCoefficient;
    }

    @Override
    public String toString() {
        return String.format("PowerSupplyUnit[model='%s', maxWatts=%d, efficiency=%.2f]", model, maxOutputWatts, efficiencyCoefficient);
    }
}

abstract class AbstractHardwareComponent {
    private final String modelName;
    private final double priceUah;

    public AbstractHardwareComponent(String modelName, double priceUah) {
        this.modelName = modelName;
        if (priceUah < 0) {
            this.priceUah = 0.0;
        } else {
            this.priceUah = priceUah;
        }
    }

    public String getModelName() {
        return modelName;
    }

    public double getPriceUah() {
        return priceUah;
    }

    public abstract int getPowerWatts();

    public abstract double getBenchmarkScore();

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "[%s] %s (%d Вт | Score: %.1f)",
                getCategory(), modelName, getPowerWatts(), getBenchmarkScore());
    }
}

class ProcessorUnit extends AbstractHardwareComponent {
    private final String socket;
    private final int cores;
    private final double boostClockGhz;
    private final int tdpWatts;

    public ProcessorUnit(String modelName, double priceUah, String socket, int cores, double boostClockGhz, int tdpWatts) {
        super(modelName, priceUah);
        this.socket = socket;
        this.cores = cores;
        this.boostClockGhz = boostClockGhz;
        this.tdpWatts = tdpWatts;
    }

    public String getSocket() {
        return socket;
    }

    @Override
    public int getPowerWatts() {
        return tdpWatts;
    }

    @Override
    public double getBenchmarkScore() {
        return cores * boostClockGhz * 120.0;
    }

    @Override
    public String getCategory() {
        return "CPU";
    }
}

class GraphicsAccelerator extends AbstractHardwareComponent {
    private final int vramGb;
    private final int tensorCores;
    private final int boardPowerWatts;

    public GraphicsAccelerator(String modelName, double priceUah, int vramGb, int tensorCores, int boardPowerWatts) {
        super(modelName, priceUah);
        this.vramGb = vramGb;
        this.tensorCores = tensorCores;
        this.boardPowerWatts = boardPowerWatts;
    }

    @Override
    public int getPowerWatts() {
        return boardPowerWatts;
    }

    @Override
    public double getBenchmarkScore() {
        return vramGb * 250.0 + tensorCores * 1.5;
    }

    @Override
    public String getCategory() {
        return "GPU";
    }
}

class NvmeSsdDrive extends AbstractHardwareComponent {
    private final int capacityGb;
    private final int readSpeedMbSec;
    private final int[] speedSamples;

    public NvmeSsdDrive(String modelName, double priceUah, int capacityGb, int readSpeedMbSec, int[] speedSamples) {
        super(modelName, priceUah);
        this.capacityGb = capacityGb;
        this.readSpeedMbSec = readSpeedMbSec;
        if (speedSamples == null) {
            this.speedSamples = new int[0];
        } else {
            this.speedSamples = new int[speedSamples.length];
            for (int i = 0; i < speedSamples.length; i++) {
                this.speedSamples[i] = speedSamples[i];
            }
        }
    }

    public int[] getSpeedSamples() {
        int[] copy = new int[speedSamples.length];
        for (int i = 0; i < speedSamples.length; i++) {
            copy[i] = speedSamples[i];
        }
        return copy;
    }

    @Override
    public int getPowerWatts() {
        return 12;
    }

    @Override
    public double getBenchmarkScore() {
        return readSpeedMbSec * 0.25;
    }

    @Override
    public String getCategory() {
        return "NVMe SSD";
    }
}

class WorkstationPC {
    private final String buildName;
    private final Motherboard motherboard;
    private final PowerSupplyUnit psu;
    private final AbstractHardwareComponent[] components;
    private int componentCount;

    public WorkstationPC(String buildName, Motherboard motherboard, PowerSupplyUnit psu) {
        this.buildName = buildName;
        this.motherboard = motherboard;
        this.psu = psu;
        this.components = new AbstractHardwareComponent[motherboard.getMaxExpansionSlots()];
        this.componentCount = 0;
    }

    public boolean installComponent(AbstractHardwareComponent comp) {
        if (comp == null) {
            System.out.println("❌ Помилка інженера: порожня комплектуюча!");
            return false;
        }
        if (componentCount >= motherboard.getMaxExpansionSlots()) {
            System.out.println("❌ Помилка інженера: немає вільних слотів розширення!");
            return false;
        }
        if (comp.getCategory().equals("CPU")) {
            ProcessorUnit cpu = (ProcessorUnit) comp;
            if (!motherboard.isSocketCompatible(cpu.getSocket())) {
                System.out.println("❌ Відмова: Процесор " + cpu.getModelName() + " (" + cpu.getSocket()
                        + ") несумісний із платою " + motherboard.getModel() + " (Сокет " + motherboard.getSocketType() + ")!");
                return false;
            }
        }
        int projectedPower = getTotalPowerWatts() + comp.getPowerWatts();
        if (projectedPower > psu.getSafeContinuousWatts()) {
            System.out.println("❌ Відмова: " + comp.getModelName() + " споживає " + comp.getPowerWatts()
                    + " Вт, сумарне навантаження " + projectedPower + " Вт перевищить безпечний ліміт БЖ "
                    + psu.getSafeContinuousWatts() + " Вт!");
            return false;
        }
        components[componentCount] = comp;
        componentCount++;
        System.out.println("✅ Встановлено: " + comp);
        return true;
    }

    public int getTotalPowerWatts() {
        int sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum = sum + components[i].getPowerWatts();
        }
        return sum;
    }

    public double getTotalSystemScore() {
        double sum = 0.0;
        for (int i = 0; i < componentCount; i++) {
            sum = sum + components[i].getBenchmarkScore();
        }
        return sum;
    }

    public double getTotalBuildPrice() {
        double sum = 0.0;
        for (int i = 0; i < componentCount; i++) {
            sum = sum + components[i].getPriceUah();
        }
        return sum;
    }

    @Override
    public String toString() {
        int totalPower = getTotalPowerWatts();
        double wallPower = psu.calculateWallPowerDraw(totalPower);
        String status = (totalPower <= psu.getSafeContinuousWatts()) ? "СТАБІЛЬНО!" : "РИЗИК ПЕРЕВАНТАЖЕННЯ БЖ!";
        StringBuilder sb = new StringBuilder();
        sb.append("[ПАСПОРТ ЗБІРКИ '").append(buildName).append("']:\n");
        sb.append("  Материнська плата: ").append(motherboard.getModel())
                .append(" (Socket: ").append(motherboard.getSocketType())
                .append(", Слотів: ").append(componentCount).append("/").append(motherboard.getMaxExpansionSlots())
                .append(")\n");
        sb.append(String.format(java.util.Locale.US, "  Блок живлення:     %s (Безпечний ліміт 85%%: %d Вт, ККД: %.0f%%)%n",
                psu.getModel(), psu.getSafeContinuousWatts(), psu.getEfficiencyCoefficient() * 100));
        sb.append(String.format(java.util.Locale.US, "  Внутрішнє споживання: %d Вт (З розетки з урахуванням ККД: %.2f Вт) -> %s%n",
                totalPower, wallPower, status));
        sb.append(String.format(java.util.Locale.US, "  Сумарний індекс продуктивності: %.2f балів%n", getTotalSystemScore()));
        sb.append(String.format(java.util.Locale.US, "  Загальна вартість конфігурації: %.2f грн", getTotalBuildPrice()));
        return sb.toString();
    }
}

public class Task2WorkstationConfigurator {
    public static void main(String[] args) {
        System.out.println("================ КОНФІГУРАТОР РОБОЧОЇ СТАНЦІЇ (КОМПОЗИЦІЯ) ================");

        Motherboard motherboard = new Motherboard("ASUS ROG B650", "AM5", 4);
        PowerSupplyUnit psu = new PowerSupplyUnit("Seasonics 750W Gold", 750, 0.90);
        WorkstationPC pc = new WorkstationPC("DeepLearning-Rig", motherboard, psu);

        System.out.println("[ПЕРЕВІРКА СУМІСНОСТІ]:");
        pc.installComponent(new ProcessorUnit("Intel Core i9", 45000.0, "LGA1700", 24, 6.0, 253));
        pc.installComponent(new ProcessorUnit("AMD Ryzen 9 7950X", 33000.0, "AM5", 16, 5.7, 170));
        pc.installComponent(new GraphicsAccelerator("NVIDIA RTX 4080 Super", 41000.0, 16, 552, 320));

        int[] samples = {7401, 7450, 7437};
        NvmeSsdDrive ssd = new NvmeSsdDrive("Samsung 990 Pro 2TB", 4900.0, 2000, 7450, samples);
        samples[0] = 100;
        pc.installComponent(ssd);

        System.out.println();
        System.out.println(pc);

        System.out.println("===========================================================================");
    }
}
