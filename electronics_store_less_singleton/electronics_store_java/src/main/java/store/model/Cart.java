package store.model;

import store.memento.CartMemento;
import store.observer.Event;
import store.observer.EventBus;
import store.singleton.MementoStorage;
import store.singleton.ProductRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private Customer              customer;
    private Map<String, Integer>  items;   // productId -> qty
    private MementoStorage        mementoStorage;
    private EventBus              eventBus;

    public Cart(Customer customer) {
        this.customer       = customer;
        this.items          = new LinkedHashMap<>();
        this.mementoStorage = MementoStorage.getInstance();
        this.eventBus       = EventBus.getInstance();
    }

    // Memento: знімок перед кожною зміною

    public void snapshot() {
        mementoStorage.save(customer.getCustomerId(),
                new CartMemento(new LinkedHashMap<>(items)));
    }

    //  Публічні методи
    public boolean addItem(Product product, int qty) {
        ProductRepository repo = ProductRepository.getInstance();
        Product p = repo.get(product.getProductId());
        if (p == null || p.getStock() < qty) {
            System.out.println("   Недостатньо товару '" + product.getName() + "' на складі");
            return false;
        }
        snapshot();
        items.merge(product.getProductId(), qty, Integer::sum);
        eventBus.publish(new Event("CART_ADD",
                Map.of("product", product.getName(),
                        "qty", qty,
                        "customer", customer.getName())));
        System.out.println("   Додано: " + product.getName() + " x" + qty);
        return true;
    }

    public boolean removeItem(String productId) {
        if (!items.containsKey(productId)) {
            System.out.println("   Товар не знайдено в кошику");
            return false;
        }
        snapshot();
        items.remove(productId);
        eventBus.publish(new Event("CART_REMOVE", Map.of("productId", productId)));
        System.out.println("    Товар видалено з кошика");
        return true;
    }

    public void undo() {
        CartMemento memento = mementoStorage.restore(customer.getCustomerId());
        if (memento != null) {
            items.clear();
            items.putAll(memento.getState());
            System.out.println("    Кошик відновлено до попереднього стану");
        } else {
            System.out.println("   Немає стану для відновлення");
        }
    }

    public void clear() { items.clear(); }

    // Підрахунок вартості

    public double getSubtotal() {
        ProductRepository repo = ProductRepository.getInstance();
        double total = 0;
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            Product p = repo.get(e.getKey());
            if (p != null) total += p.getPrice() * e.getValue();
        }
        return total;
    }

    public double getTotal() {
        return getSubtotal() * (1 - customer.getDiscount());
    }

    // Геттери

    public Customer             getCustomer() { return customer; }
    public Map<String, Integer> getItems()    { return items; }
    public boolean              isEmpty()     { return items.isEmpty(); }
}
