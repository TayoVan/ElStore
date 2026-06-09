package store.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Laptop extends ElectronicDevice {

    private String cpu;
    private int    ramGb;
    private int    ssdGb;
    private double displayInch;

    public Laptop(String name, double price, int warrantyMonths, int stock,
                  String cpu, int ramGb, int ssdGb, double displayInch) {
        super(name, price, warrantyMonths, stock);
        this.cpu         = cpu;
        this.ramGb       = ramGb;
        this.ssdGb       = ssdGb;
        this.displayInch = displayInch;
    }

    @Override
    public Map<String, String> getSpecs() {
        Map<String, String> specs = new LinkedHashMap<>();
        specs.put("CPU",    cpu);
        specs.put("RAM",    ramGb + " GB");
        specs.put("SSD",    ssdGb + " GB");
        specs.put("Екран",  displayInch + "\"");
        return specs;
    }

    @Override
    public String display() {
        return String.format(" %-28s | %8.0f₴ | %s | RAM:%dGB | SSD:%dGB | %s (%d шт)",
                getName(), getPrice(), cpu, ramGb, ssdGb,
                isAvailable() ? "Yes" : "No", getStock());
    }

    @Override
    public String getCategory() { return "Ноутбуки"; }

    public String getCpu()         { return cpu; }
    public int    getRamGb()       { return ramGb; }
    public int    getSsdGb()       { return ssdGb; }
    public double getDisplayInch() { return displayInch; }
}
