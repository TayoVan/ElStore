package store.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {

    public enum LoyaltyTier {
        BRONZE  ("Bronze",   0,      0.00),
        SILVER  ("Silver",   5_000,  0.05),
        GOLD    ("Gold",    20_000,  0.10),
        PLATINUM("Platinum",50_000,  0.15);

        private String label;
        private double threshold;
        private double discount;

        LoyaltyTier(String label, double threshold, double discount) {
            this.label     = label;
            this.threshold = threshold;
            this.discount  = discount;
        }

        public String getLabel()    { return label; }
        public double getDiscount() { return discount; }

        public static LoyaltyTier forSpent(double spent) {
            LoyaltyTier result = BRONZE;
            for (var tier : values()) {
                if (spent >= tier.threshold) result = tier;
            }
            return result;
        }

        public LoyaltyTier next() {
            LoyaltyTier[] vals = values();
            int idx = ordinal() + 1;
            return (idx < vals.length) ? vals[idx] : null;
        }

        public double getThreshold() { return threshold; }
    }

    // Поля

    private String     customerId;
    private String     name;
    private String     email;
    private double           totalSpent;
    private List<Order> orders;

    public Customer(String name, String email) {
        this.customerId = UUID.randomUUID().toString().substring(0, 8);
        this.name       = name;
        this.email      = email;
        this.totalSpent = 0.0;
        this.orders     = new ArrayList<>();
    }


    public LoyaltyTier getTier()     { return LoyaltyTier.forSpent(totalSpent); }
    public double       getDiscount(){ return getTier().getDiscount(); }

    public void addSpent(double amount) { totalSpent += amount; }
    public void addOrder(Order order)   { orders.add(order); }


    public String      getCustomerId() { return customerId; }
    public String      getName()       { return name; }
    public String      getEmail()      { return email; }
    public double      getTotalSpent() { return totalSpent; }
    public List<Order> getOrders()     { return orders; }
    public void        setTotalSpent(double v) { totalSpent = v; }

    @Override
    public String toString() {
        LoyaltyTier tier = getTier();
        return String.format(" %s (%s) | Рівень: %s | Витрачено: %.0f₴ | Знижка: %.0f%%",
                name, email, tier.getLabel(), totalSpent, tier.getDiscount() * 100);
    }
}
