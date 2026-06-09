package store.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Event {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String              type;
    private Map<String, Object> data;
    private String              timestamp;

    public Event(String type, Map<String, Object> data) {
        this.type      = type;
        this.data      = Map.copyOf(data);
        this.timestamp = LocalDateTime.now().format(FMT);
    }

    public String              getType()      { return type; }
    public Map<String, Object> getData()      { return data; }
    public String              getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s", timestamp, type, data);
    }
}
