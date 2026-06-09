package store.service;

import store.model.Product;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComparisonService {

    private static final int COL_WIDTH = 22;

    public void compare(List<Product> products) {
        if (products == null || products.size() < 2) {
            System.out.println("   Для порівняння потрібно мінімум 2 товари");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ПОРІВНЯННЯ ТОВАРІВ");
        System.out.println("=".repeat(60));

        // Заголовок
        System.out.printf("%-20s", "Характеристика");
        for (Product p : products) {
            System.out.printf("%-" + COL_WIDTH + "s", shorten(p.getName(), COL_WIDTH - 1));
        }
        System.out.println();
        System.out.println("-".repeat(20 + COL_WIDTH * products.size()));

        // Ціна
        System.out.printf("%-20s", "Ціна");
        products.forEach(p -> System.out.printf("%-" + COL_WIDTH + "s",
                String.format("%.0f₴", p.getPrice())));
        System.out.println();

        // Гарантія
        System.out.printf("%-20s", "Гарантія");
        products.forEach(p -> System.out.printf("%-" + COL_WIDTH + "s",
                p.getWarrantyMonths() + " міс."));
        System.out.println();

        // Наявність
        System.out.printf("%-20s", "Наявність");
        products.forEach(p -> System.out.printf("%-" + COL_WIDTH + "s",
                p.isAvailable() ? " Є (" + p.getStock() + "шт)" : " Нема"));
        System.out.println();

        System.out.println("-".repeat(20 + COL_WIDTH * products.size()));

        // Об'єднані специфікації
        Set<String> allKeys = new LinkedHashSet<>();
        for (Product p : products) allKeys.addAll(p.getSpecs().keySet());

        for (String key : allKeys) {
            System.out.printf("%-20s", key);
            for (Product p : products) {
                Map<String, String> specs = p.getSpecs();
                System.out.printf("%-" + COL_WIDTH + "s",
                        specs.getOrDefault(key, "—"));
            }
            System.out.println();
        }

        System.out.println("=".repeat(60));
    }

    private String shorten(String s, int max) {
        return (s.length() > max) ? s.substring(0, max - 1) + "…" : s;
    }
}
