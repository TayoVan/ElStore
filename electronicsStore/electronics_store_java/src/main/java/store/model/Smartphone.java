package store.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Smartphone extends Product {

    private final int    ramGb;
    private final int    storageGb;
    private final int    cameraMp;
    private final int    batteryMah;

    public Smartphone(String name, double price, int warrantyMonths, int stock,
                      int ramGb, int storageGb, int cameraMp, int batteryMah) {
        super(name, price, warrantyMonths, stock);
        this.ramGb      = ramGb;
        this.storageGb  = storageGb;
        this.cameraMp   = cameraMp;
        this.batteryMah = batteryMah;
    }

    @Override
    public Map<String, String> getSpecs() {
        Map<String, String> specs = new LinkedHashMap<>();
        specs.put("RAM",      ramGb + " GB");
        specs.put("Пам'ять",  storageGb + " GB");
        specs.put("Камера",   cameraMp + " MP");
        specs.put("Батарея",  batteryMah + " mAh");
        return specs;
    }

    @Override
    public String display() {
        return String.format(" %-28s | %8.0f₴ | RAM:%dGB | %dGB | %dMP | %s (%d шт)",
                getName(), getPrice(), ramGb, storageGb, cameraMp,
                isAvailable() ? "Yes" : "No", getStock());
    }

    @Override
    public String getCategory() { return "Смартфони"; }

    // Геттери
    public int getRamGb()      { return ramGb; }
    public int getStorageGb()  { return storageGb; }
    public int getCameraMp()   { return cameraMp; }
    public int getBatteryMah() { return batteryMah; }
}
