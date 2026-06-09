package store;

import store.factory.*;
import store.model.*;
import store.observer.*;
import store.singleton.*;
import store.ui.ConsoleUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        //  Observer: підпис спостерігачів
        EventBus eventBus = EventBus.getInstance();

        StatisticsObserver statistics = new StatisticsObserver();
        eventBus.subscribe("*", statistics);                        // всі події

        LoggerObserver logger = new LoggerObserver(               // лише замовлення
                "ORDER_PLACED", "ORDER_SHIPPED", "ORDER_DELIVERED");
        eventBus.subscribe("ORDER_PLACED",    logger);
        eventBus.subscribe("ORDER_SHIPPED",   logger);
        eventBus.subscribe("ORDER_DELIVERED", logger);

        //  Abstract Factory:  товари
        ProductRepository productRepo = ProductRepository.getInstance();

        ProductFactory factory = new ProductFactory();

        productRepo.add(factory.createBudgetSmartphone());
        productRepo.add(factory.createPremiumSmartphone());
        productRepo.add(factory.createBudgetLaptop());
        productRepo.add(factory.createPremiumLaptop());
        productRepo.add(factory.createBudgetTablet());
        productRepo.add(factory.createPremiumTablet());

        // Додаткові товари
        productRepo.add(new Smartphone("Xiaomi 14T", 22_999, 12, 12,
                12, 256, 108, 5_000));
        productRepo.add(new Laptop("Lenovo IdeaPad 5", 32_999, 24, 6,
                "AMD Ryzen 5", 16, 512, 15.6));
        productRepo.add(new Tablet("Lenovo Tab P12", 14_999, 12, 8,
                12.7, 8, 128, false));

        //  Singleton: ініціалізуємо покупців
        CustomerRepository customerRepo = CustomerRepository.getInstance();

        List<Customer> customers = new ArrayList<>();
        Object[][] seed = {
                {"Олег Мороз",        "oleg@mail.ua",    3_000.0},
                {"Марія Коваль",      "maria@mail.ua",  25_000.0},
                {"Дмитро Шевченко",   "dmytro@mail.ua", 55_000.0},
        };
        for (var row : seed) {
            Customer c = new Customer((String) row[0], (String) row[1]);
            c.setTotalSpent((double) row[2]);
            customerRepo.add(c);
            customers.add(c);
        }

        // Публікуємо подію старту системи
        eventBus.publish(new Event("SYSTEM_START",
                Map.of("products", productRepo.size(),
                        "customers", customers.size())));

        //  Запуск консольного UI
        new ConsoleUI(statistics).start(customers);
    }
}
