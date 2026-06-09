package store.observer;

import java.util.LinkedHashMap;
import java.util.Map;


public class StatisticsObserver implements StoreObserver {

    private Map<String, Integer> eventCounts = new LinkedHashMap<>();
    private double totalRevenue = 0.0;

    @Override
    public void onEvent(Event event) {
        eventCounts.merge(event.getType(), 1, Integer::sum);

        if ("ORDER_PLACED".equals(event.getType())) {
            Object total = event.getData().get("total");
            if (total instanceof Number) {
                totalRevenue += ((Number) total).doubleValue();
            }
        }
    }

    public void printStats() {
        System.out.println("\n" + "=".repeat(52));
        System.out.println("  СТАТИСТИКА ПОДІЙ");
        System.out.println("=".repeat(52));
        if (eventCounts.isEmpty()) {
            System.out.println("  (немає подій)");
        } else {
            eventCounts.forEach((type, count) ->
                    System.out.printf("  %-30s : %d%n", type, count));
        }
        System.out.printf("   Загальний дохід               : %.2f₴%n", totalRevenue);
        System.out.println("=".repeat(52));
    }

    public Map<String, Integer> getEventCounts() { return Map.copyOf(eventCounts); }
    public double               getTotalRevenue() { return totalRevenue; }
}
