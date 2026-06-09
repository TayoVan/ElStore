package store.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Tablet extends Product {

    private final double  displayInch;
    private final int     ramGb;
    private final int     storageGb;
    private final boolean hasLte;

    public Tablet(String name, double price, int warrantyMonths, int stock,
                  double displayInch, int ramGb, int storageGb, boolean hasLte) {
        super(name, price, warrantyMonths, stock);
        this.displayInch = displayInch;
        this.ramGb       = ramGb;
        this.storageGb   = storageGb;
        this.hasLte      = hasLte;
    }

    @Override
    public Map<String, String> getSpecs() {
        Map<String, String> specs = new LinkedHashMap<>();
        specs.put("Екран",    displayInch + "\"");
        specs.put("RAM",      ramGb + " GB");
        specs.put("Пам'ять",  storageGb + " GB");
        specs.put("LTE",      hasLte ? "Так" : "Ні");
        return specs;
    }

    @Override
    public String display() {
        return String.format(" %-28s | %8.0f₴ | %.1f\" | RAM:%dGB | %s | %s (%d шт)",
                getName(), getPrice(), displayInch, ramGb,
                hasLte ? "LTE" : "WiFi",
                isAvailable() ? "Yes" : "No", getStock());
    }

    @Override
    public String getCategory() { return "Планшети"; }

    public double  getDisplayInch() { return displayInch; }
    public int     getRamGb()       { return ramGb; }
    public int     getStorageGb()   { return storageGb; }
    public boolean isHasLte()       { return hasLte; }
}
