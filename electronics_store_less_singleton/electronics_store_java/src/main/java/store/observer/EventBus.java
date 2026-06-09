package store.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    // Singleton
    public EventBus() {}

    

    //  Стан

    private Map<String, List<StoreObserver>> subscribers = new ConcurrentHashMap<>();
    private List<Event>                       history     = new ArrayList<>();

    // Підписка
    public void subscribe(String eventType, StoreObserver observer) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(observer);
    }

    public void unsubscribe(String eventType, StoreObserver observer) {
        List<StoreObserver> list = subscribers.get(eventType);
        if (list != null) list.remove(observer);
    }

    // Публікація

    public void publish(Event event) {
        history.add(event);

        // Сповіщаємо підписників конкретного типу
        List<StoreObserver> specific = subscribers.get(event.getType());
        if (specific != null) specific.forEach(o -> o.onEvent(event));

        // Сповіщаємо підписників-"*"
        List<StoreObserver> all = subscribers.get("*");
        if (all != null) all.forEach(o -> o.onEvent(event));
    }

    // Геттери

    public List<Event> getHistory() { return List.copyOf(history); }
    public int         getEventCount() { return history.size(); }
}
