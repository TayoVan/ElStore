package store.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ElectronicDevice extends Product {

    protected ElectronicDevice(String name, double price, int warrantyMonths, int stock) {
        super(name, price, warrantyMonths, stock);
    }

    protected Map<String, String> createSpecs() {
        return new LinkedHashMap<>();
    }
}
