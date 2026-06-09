package store.factory;

import store.model.Laptop;
import store.model.Product;

public class LaptopFactory extends ProductFactory {

    @Override
    public Product createBudget() {
        return new Laptop(
                "Acer Aspire 3", 18_999, 24, 8,
                "Intel Core i3", 8, 256, 15.6);
    }

    @Override
    public Product createPremium() {
        return new Laptop(
                "MacBook Pro 14", 89_999, 12, 3,
                "Apple M3 Pro", 18, 512, 14.2);
    }
}
