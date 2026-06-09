package store.model;

import java.util.Map;
import java.util.UUID;

public abstract class Product {

    private String productId;
    private String name;
    private double price;
    private int warrantyMonths;
    private int stock;

    public Product(String name, double price, int warrantyMonths, int stock) {
        this.productId     = UUID.randomUUID().toString().substring(0, 8);
        this.name          = name;
        this.price         = price;
        this.warrantyMonths = warrantyMonths;
        this.stock         = stock;
    }

    //  Абстрактні методи

    public abstract Map<String, String> getSpecs();

    //Повертає рядковий опис товару для виводу в інтерфейсі.
    public abstract String display();

    // Повертає назву категорії (клас товару).
    public abstract String getCategory();

    //  Загальна логіка

    public boolean isAvailable() {
        return stock > 0;
    }

    public void decreaseStock(int qty) {
        if (stock < qty) throw new IllegalStateException("Недостатньо товару на складі: " + name);
        stock -= qty;
    }

    // Геттери / Сеттери

    public String getProductId()     { return productId; }
    public String getName()          { return name; }
    public double getPrice()         { return price; }
    public int    getWarrantyMonths(){ return warrantyMonths; }
    public int    getStock()         { return stock; }

    @Override
    public String toString() { return display(); }
}
