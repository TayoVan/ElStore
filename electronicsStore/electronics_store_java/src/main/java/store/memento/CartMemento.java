package store.memento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartMemento {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final Map<String, Integer> state;
    private final String               createdAt;

    public CartMemento(Map<String, Integer> state) {
        this.state     = new LinkedHashMap<>(state); // захисна копія
        this.createdAt = LocalDateTime.now().format(FMT);
    }


    public Map<String, Integer> getState() {
        return new LinkedHashMap<>(state);
    }

    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "CartMemento[" + createdAt + ", items=" + state.size() + "]";
    }
}
