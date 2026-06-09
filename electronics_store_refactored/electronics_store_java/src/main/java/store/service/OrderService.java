package store.service;

import store.model.Cart;
import store.model.Customer;
import store.model.Order;
import store.model.Product;
import store.observer.Event;
import store.observer.EventBus;
import store.singleton.ProductRepository;

import java.util.Map;

public class OrderService {

    private final EventBus          eventBus   = EventBus.getInstance();
    private final ProductRepository productRepo = ProductRepository.getInstance();

    public Order placeOrder(Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Кошик порожній");
        }

        // Перевірка наявності перед фіксацією
        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            Product p = productRepo.get(e.getKey());
            if (p == null || p.getStock() < e.getValue()) {
                throw new IllegalStateException(
                        "Товар '" + (p != null ? p.getName() : e.getKey()) +
                                "' недоступний у необхідній кількості");
            }
        }

        double total = cart.getTotal();
        Customer customer = cart.getCustomer();

        // Зменшуємо залишки
        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            productRepo.get(e.getKey()).decreaseStock(e.getValue());
        }

        // Створюємо замовлення
        Order order = new Order(customer, cart.getItems(), total);
        order.setStatus(Order.Status.CONFIRMED);

        // Нараховуємо витрати покупцю (для рівня лояльності)
        customer.addSpent(total);
        customer.addOrder(order);

        cart.clear();

        eventBus.publish(new Event("ORDER_PLACED", Map.of(
                "orderId",    order.getOrderId(),
                "customer",   customer.getName(),
                "total",      total,
                "itemsCount", order.getItems().size()
        )));

        return order;
    }


    public void shipOrder(Order order) {
        order.setStatus(Order.Status.SHIPPED);
        eventBus.publish(new Event("ORDER_SHIPPED",
                Map.of("orderId", order.getOrderId())));
    }

    public void deliverOrder(Order order) {
        order.setStatus(Order.Status.DELIVERED);
        eventBus.publish(new Event("ORDER_DELIVERED",
                Map.of("orderId", order.getOrderId())));
    }
}
