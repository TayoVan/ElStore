package store.singleton;

import store.model.Product;

import java.util.*;

public class ProductRepository {

    // Singleton
    private static ProductRepository instance;

    private ProductRepository() {}

    public static synchronized ProductRepository getInstance() {
        if (instance == null) instance = new ProductRepository();
        return instance;
    }

    // Сховище

    private final Map<String, Product> products = new LinkedHashMap<>();

    public void add(Product product) {
        products.put(product.getProductId(), product);
    }

    public Product get(String productId) {
        return products.get(productId);
    }

    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    public List<Product> findByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product p : products.values()) {
            if (category.equalsIgnoreCase(p.getCategory())) result.add(p);
        }
        return result;
    }

    public int size() { return products.size(); }
}
