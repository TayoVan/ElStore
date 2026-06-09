package store.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Order {

    public enum Status {
        PENDING    ("Очікує"),
        CONFIRMED  ("Підтверджено"),
        SHIPPED    ("Відправлено"),
        DELIVERED  ("Доставлено"),
        CANCELLED  ("Скасовано");

        private final String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    // Поля

    private final String              orderId;
    private final Customer            customer;
    private final Map<String, Integer> items;   // productId -> qty
    private final double              total;
    private       Status              status;
    private final String              createdAt;

    public Order(Customer customer, Map<String, Integer> items, double total) {
        this.orderId   = UUID.randomUUID().toString().substring(0, 8);
        this.customer  = customer;
        this.items     = new LinkedHashMap<>(items);
        this.total     = total;
        this.status    = Status.PENDING;
        this.createdAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    // Геттери / зміна статусу

    public void setStatus(Status status) { this.status = status; }

    public String              getOrderId()  { return orderId; }
    public Customer            getCustomer() { return customer; }
    public Map<String, Integer> getItems()   { return items; }
    public double              getTotal()    { return total; }
    public Status              getStatus()   { return status; }
    public String              getCreatedAt(){ return createdAt; }

    @Override
    public String toString() {
        return String.format("   Замовлення #%s | %s | %.0f₴ | %s",
                orderId, status.getLabel(), total, createdAt);
    }
}
