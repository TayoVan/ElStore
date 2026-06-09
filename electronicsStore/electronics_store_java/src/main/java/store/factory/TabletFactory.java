package store.factory;

import store.model.Product;
import store.model.Tablet;

public class TabletFactory extends ProductFactory {

    @Override
    public Product createBudget() {
        return new Tablet(
                "Samsung Tab A9", 7_999, 12, 15,
                10.1, 4, 64, false);
    }

    @Override
    public Product createPremium() {
        return new Tablet(
                "iPad Pro 13", 42_999, 12, 6,
                13.0, 8, 256, true);
    }
}
