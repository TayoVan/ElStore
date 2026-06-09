package store.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ElectronicDevice extends Product {

    public ElectronicDevice(String name, double price, int warrantyMonths, int stock) {
        super(name, price, warrantyMonths, stock);
    }

    public Map<String, String> createSpecs() {
        return new LinkedHashMap<>();
    }
}
