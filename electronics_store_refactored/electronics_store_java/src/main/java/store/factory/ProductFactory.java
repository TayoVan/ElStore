package store.factory;

import store.model.*;

public class ProductFactory {

    public Product createBudgetSmartphone() {
        return new Smartphone("Samsung Galaxy A25", 11999, 24, 15, 8, 128, 50, 5000);
    }

    public Product createPremiumSmartphone() {
        return new Smartphone("iPhone 15 Pro", 54999, 12, 4, 8, 256, 48, 3274);
    }

    public Product createBudgetLaptop() {
        return new Laptop("Acer Aspire 3", 18999, 24, 8, "Intel Core i3", 8, 256, 15.6);
    }

    public Product createPremiumLaptop() {
        return new Laptop("MacBook Pro 14", 89999, 12, 3, "Apple M3 Pro", 18, 512, 14.2);
    }

    public Product createBudgetTablet() {
        return new Tablet("Samsung Galaxy Tab A9", 8999, 24, 12, 11.0, 4, 64, false);
    }

    public Product createPremiumTablet() {
        return new Tablet("iPad Pro 13", 59999, 12, 2, 13.0, 16, 512, true);
    }
}
