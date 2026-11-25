package semester1.practice12.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

abstract class Transport {
    private final String name;
    private final double speedKmH;

    public Transport(String name, double speedKmH) {
        if (!Double.isFinite(speedKmH) || speedKmH <= 0) throw new IllegalArgumentException("Швидкість має бути додатною");
        this.name = name;
        this.speedKmH = speedKmH;
    }

    public String getName() { return name; }
    public double getSpeedKmH() { return speedKmH; }
    public double calculateTimeHours(int distanceKm) { return distanceKm / speedKmH; }
    public abstract double calculateCostUah(int distanceKm);
}

class Bus extends Transport {
    public Bus() { super("Bus", 80.0); }

    @Override
    public double calculateCostUah(int distanceKm) { return distanceKm * 2.5; }
}

class Train extends Transport {
    public Train() { super("Train", 100.0); }

    @Override
    public double calculateCostUah(int distanceKm) { return distanceKm * 1.8 + 150.0; }
}

class DeliveryTruck extends Transport {
    public DeliveryTruck() { super("DeliveryTruck", 70.0); }

    @Override
    public double calculateCostUah(int distanceKm) { return distanceKm * 4.0 + 300.0; }
}

class TripOrder {
    private final String orderId;
    private final String fromCity;
    private final String toCity;
    private final Transport transport;

    public TripOrder(String orderId, String fromCity, String toCity, Transport transport) {
        this.orderId = Objects.requireNonNull(orderId);
        this.fromCity = Objects.requireNonNull(fromCity);
        this.toCity = Objects.requireNonNull(toCity);
        this.transport = Objects.requireNonNull(transport);
    }

    public String getOrderId() { return orderId; }
    public String getFromCity() { return fromCity; }
    public String getToCity() { return toCity; }
    public Transport getTransport() { return transport; }

    @Override
    public boolean equals(Object o) {
        return o instanceof TripOrder && orderId.equals(((TripOrder) o).orderId);
    }

    @Override
    public int hashCode() { return orderId.hashCode(); }
}

class RouteDispatcher {
    private final ArrayList<String> cities;
    private final int[][] distances;
    private final HashSet<String> closedCities;
    private final ArrayList<TripOrder> orders = new ArrayList<>();

    public RouteDispatcher(ArrayList<String> cities, int[][] distances, HashSet<String> closedCities) {
        if (distances.length != cities.size()) throw new IllegalArgumentException("Невірний розмір матриці");
        this.cities = new ArrayList<>(cities);
        this.closedCities = new HashSet<>(closedCities);
        this.distances = new int[distances.length][];
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] == null || distances[i].length != cities.size()) {
                throw new IllegalArgumentException("Матриця має бути квадратною");
            }
            this.distances[i] = distances[i].clone();
        }
    }

    public void addOrder(TripOrder order) { orders.add(Objects.requireNonNull(order)); }
    public ArrayList<TripOrder> getOrdersCopy() { return new ArrayList<>(orders); }

    public int getDistance(String from, String to) {
        int i = cities.indexOf(from);
        int j = cities.indexOf(to);
        return i >= 0 && j >= 0 ? distances[i][j] : 0;
    }

    public HashSet<String> getDirectNeighbors(String city) {
        HashSet<String> neighbors = new HashSet<>();
        int index = cities.indexOf(city);
        if (index >= 0) {
            for (int j = 0; j < cities.size(); j++) {
                if (distances[index][j] > 0) neighbors.add(cities.get(j));
            }
        }
        return neighbors;
    }

    public HashSet<String> getCommonNeighbors(String cityA, String cityB) {
        HashSet<String> result = getDirectNeighbors(cityA);
        result.retainAll(getDirectNeighbors(cityB));
        return result;
    }

    public HashSet<String> getExclusiveNeighbors(String cityA, String cityB) {
        HashSet<String> result = getDirectNeighbors(cityA);
        result.removeAll(getDirectNeighbors(cityB));
        return result;
    }

    public int filterInvalidOrders() {
        HashSet<String> seenIds = new HashSet<>();
        Iterator<TripOrder> it = orders.iterator();
        int removed = 0;
        while (it.hasNext()) {
            TripOrder order = it.next();
            String reason;
            if (!seenIds.add(order.getOrderId())) reason = "ДУБЛІКАТ";
            else if (closedCities.contains(order.getFromCity()) || closedCities.contains(order.getToCity())) {
                reason = "МІСТО ЗАКРИТЕ";
            } else if (getDistance(order.getFromCity(), order.getToCity()) <= 0) reason = "НЕМАЄ ДОРОГИ";
            else continue;
            System.out.println("[" + reason + "] " + order.getOrderId() + " (" + order.getFromCity()
                    + " -> " + order.getToCity() + ") — видалено!");
            it.remove();
            removed++;
        }
        return removed;
    }

    public void printReport() {
        int totalDistance = 0;
        double totalCost = 0;
        System.out.println("Схвалені рейси: " + orders.size());
        for (TripOrder order : orders) {
            int distance = getDistance(order.getFromCity(), order.getToCity());
            Transport transport = order.getTransport();
            double cost = transport.calculateCostUah(distance);
            totalDistance += distance;
            totalCost += cost;
            System.out.printf(Locale.US, "%s | %s -> %s | %d км | %s | Час: %.2f год | Вартість: %.2f грн%n",
                    order.getOrderId(), order.getFromCity(), order.getToCity(), distance,
                    transport.getName(), transport.calculateTimeHours(distance), cost);
        }
        System.out.printf(Locale.US, "Загальна відстань: %d км | Загальна вартість перевезень: %.2f грн%n",
                totalDistance, totalCost);
    }
}

public class Task4MatrixAndCityNetwork {
    public static void main(String[] args) {
        ArrayList<String> cities = new ArrayList<>(Arrays.asList("Київ", "Львів", "Одеса", "Дніпро"));
        int[][] distances = {
                {0, 540, 480, 450},
                {540, 0, 790, 0},
                {480, 790, 0, 460},
                {450, 0, 460, 0}
        };
        HashSet<String> closed = new HashSet<>(Arrays.asList("Одеса"));
        RouteDispatcher dispatcher = new RouteDispatcher(cities, distances, closed);
        System.out.println("ЛОГІСТИЧНИЙ ДИСПЕТЧЕР: МАТРИЦЯ ТА МЕРЕЖА");
        System.out.println("Прямі сусіди Києва: " + dispatcher.getDirectNeighbors("Київ"));
        System.out.println("Прямі сусіди Львова: " + dispatcher.getDirectNeighbors("Львів"));
        System.out.println("Спільні сусіди: " + dispatcher.getCommonNeighbors("Київ", "Львів"));
        System.out.println("Тільки з Києва: " + dispatcher.getExclusiveNeighbors("Київ", "Львів"));
        dispatcher.addOrder(new TripOrder("ORD-1", "Київ", "Львів", new Train()));
        dispatcher.addOrder(new TripOrder("ORD-1", "Київ", "Львів", new Bus()));
        dispatcher.addOrder(new TripOrder("ORD-2", "Київ", "Одеса", new Bus()));
        dispatcher.addOrder(new TripOrder("ORD-3", "Львів", "Дніпро", new DeliveryTruck()));
        dispatcher.addOrder(new TripOrder("ORD-4", "Київ", "Дніпро", new DeliveryTruck()));
        System.out.println("Закриті міста: " + closed);
        int removed = dispatcher.filterInvalidOrders();
        System.out.println("Видалено заявок: " + removed);
        dispatcher.printReport();
        assert removed == 3 && dispatcher.getOrdersCopy().size() == 2;
        assert dispatcher.getCommonNeighbors("Київ", "Львів").equals(closed);
        double total = 0;
        for (TripOrder order : dispatcher.getOrdersCopy()) {
            total += order.getTransport().calculateCostUah(dispatcher.getDistance(order.getFromCity(), order.getToCity()));
        }
        assert Math.abs(total - 3222) < 0.000001;
    }
}
