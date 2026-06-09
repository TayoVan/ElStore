package store.factory;

import store.model.Product;
import store.model.Smartphone;

/**
 * Конкретна фабрика смартфонів (патерн Abstract Factory).
 */
public class SmartphoneFactory extends ProductFactory {

    @Override
    public Product createBudget() {
        return new Smartphone(
                "Samsung Galaxy A15", 5_999, 12, 10,
                4, 128, 50, 5_000);
    }

    @Override
    public Product createPremium() {
        return new Smartphone(
                "iPhone 16 Pro", 54_999, 12, 5,
                8, 256, 48, 4_422);
    }
}
