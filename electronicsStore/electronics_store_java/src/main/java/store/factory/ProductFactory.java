package store.factory;

import store.model.Product;

public abstract class ProductFactory {

    public abstract Product createBudget();

    public abstract Product createPremium();
}
