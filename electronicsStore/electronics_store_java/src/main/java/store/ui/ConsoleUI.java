package store.ui;

import store.model.*;
import store.observer.EventBus;
import store.observer.StatisticsObserver;
import store.service.ComparisonService;
import store.service.OrderService;
import store.singleton.ProductRepository;

import java.util.*;


public class ConsoleUI {

    private final Scanner            scanner     = new Scanner(System.in);
    private final ProductRepository  productRepo = ProductRepository.getInstance();
    private final EventBus           eventBus    = EventBus.getInstance();
    private final StatisticsObserver stats;
    private final OrderService       orderService = new OrderService();
    private final ComparisonService  compareService = new ComparisonService();

    private Customer currentCustomer;
    private Cart     cart;

    public ConsoleUI(StatisticsObserver stats) {
        this.stats = stats;
    }


    public void start(List<Customer> customers) {
        printBanner();
        currentCustomer = chooseCustomer(customers);
        cart = new Cart(currentCustomer);

        eventBus.publish(new store.observer.Event("USER_LOGIN",
                Map.of("customer", currentCustomer.getName())));
        System.out.println("\n  Вітаємо, " + currentCustomer.getName() + "! ");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Ваш вибір");
            switch (choice) {
                case "1"  -> showCatalog();
                case "2"  -> addToCart();
                case "3"  -> removeFromCart();
                case "4"  -> cart.undo();
                case "5"  -> showCart();
                case "6"  -> placeOrder();
                case "7"  -> showOrders();
                case "8"  -> compareProducts();
                case "9"  -> showCustomerInfo();
                case "10" -> stats.printStats();
                case "0"  -> running = false;
                default   -> System.out.println("   Невірний вибір");
            }
        }

        eventBus.publish(new store.observer.Event("USER_LOGOUT",
                Map.of("customer", currentCustomer.getName())));
        stats.printStats();
        System.out.println("\n  Дякуємо за покупку! До побачення! \n");
    }



    private void showCatalog() {
        System.out.println("\n  КАТАЛОГ ТОВАРІВ:");
        Map<String, List<Product>> byCategory = new LinkedHashMap<>();
        for (Product p : productRepo.findAll()) {
            byCategory.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }
        byCategory.forEach((cat, prods) -> {
            System.out.println("\n  [" + cat + "]");
            prods.forEach(p -> System.out.println("    " + p));
        });
    }

    private void addToCart() {
        List<Product> all = productRepo.findAll();
        System.out.println("\nОберіть товар для додавання:");
        Product product = selectProduct(all);
        if (product == null) return;

        int qty = readInt("  Кількість", 1);
        cart.addItem(product, qty);
    }

    private void removeFromCart() {
        if (cart.isEmpty()) { System.out.println("  Кошик порожній"); return; }
        List<String> ids = new ArrayList<>(cart.getItems().keySet());
        System.out.println("\nТовари в кошику:");
        for (int i = 0; i < ids.size(); i++) {
            Product p = productRepo.get(ids.get(i));
            if (p != null)
                System.out.printf("  %d. %s x%d%n", i + 1, p.getName(), cart.getItems().get(ids.get(i)));
        }
        int idx = readInt("Номер для видалення (0 - скасувати)", 0) - 1;
        if (idx >= 0 && idx < ids.size()) cart.removeItem(ids.get(idx));
    }

    private void showCart() {
        System.out.printf("%n   Кошик %s:%n", currentCustomer.getName());
        if (cart.isEmpty()) { System.out.println("    (порожній)"); return; }
        cart.getItems().forEach((pid, qty) -> {
            Product p = productRepo.get(pid);
            if (p != null)
                System.out.printf("    • %-30s x%d = %.0f₴%n",
                        p.getName(), qty, p.getPrice() * qty);
        });
        double subtotal = cart.getSubtotal();
        double discount = currentCustomer.getDiscount();
        System.out.printf("    Підсумок   : %.0f₴%n", subtotal);
        if (discount > 0)
            System.out.printf("    Знижка %3.0f%%: -%.0f₴%n", discount * 100, subtotal * discount);
        System.out.printf("    До сплати  : %.0f₴%n", cart.getTotal());
    }

    private void placeOrder() {
        if (cart.isEmpty()) { System.out.println("   Кошик порожній!"); return; }
        showCart();
        String confirm = readLine("\n  Підтвердити замовлення? (т/н)");
        if (!confirm.equalsIgnoreCase("т") && !confirm.equalsIgnoreCase("y")) {
            System.out.println("    Замовлення скасовано");
            return;
        }
        try {
            Order order = orderService.placeOrder(cart);
            System.out.printf("%n   Замовлення #%s оформлено!%n", order.getOrderId());
            System.out.printf("   Сума: %.0f₴%n", order.getTotal());
            Customer.LoyaltyTier tier = currentCustomer.getTier();
            System.out.printf("   Ваш рівень: %s, знижка: %.0f%%%n",
                    tier.getLabel(), tier.getDiscount() * 100);
            Customer.LoyaltyTier next = tier.next();
            if (next != null) {
                double needed = next.getThreshold() - currentCustomer.getTotalSpent();
                System.out.printf("    До рівня %s: %.0f₴%n", next.getLabel(), needed);
            }
        } catch (Exception e) {
            System.out.println("   Помилка: " + e.getMessage());
        }
    }

    private void showOrders() {
        List<Order> orders = currentCustomer.getOrders();
        if (orders.isEmpty()) { System.out.println("  Замовлень немає"); return; }
        System.out.printf("%n  Замовлення %s:%n", currentCustomer.getName());
        for (Order o : orders) {
            System.out.println(o);
            o.getItems().forEach((pid, qty) -> {
                Product p = productRepo.get(pid);
                if (p != null)
                    System.out.printf("       • %s x%d%n", p.getName(), qty);
            });
        }
    }

    private void compareProducts() {
        List<Product> all = productRepo.findAll();
        List<Product> selected = new ArrayList<>();
        System.out.println("\nОберіть 2-3 товари для порівняння (0 - завершити):");
        while (selected.size() < 3) {
            List<Product> remaining = new ArrayList<>(all);
            remaining.removeAll(selected);
            Product p = selectProduct(remaining);
            if (p == null) break;
            selected.add(p);
            if (selected.size() >= 2) {
                String more = readLine("  Додати ще? (т/н)");
                if (!more.equalsIgnoreCase("т") && !more.equalsIgnoreCase("y")) break;
            }
        }
        if (selected.size() >= 2) compareService.compare(selected);
        else System.out.println("   Потрібно обрати мінімум 2 товари");
    }

    private void showCustomerInfo() {
        System.out.println("\n" + currentCustomer);
        Customer.LoyaltyTier next = currentCustomer.getTier().next();
        if (next != null) {
            double needed = next.getThreshold() - currentCustomer.getTotalSpent();
            System.out.printf("   До рівня %s: %.0f₴%n", next.getLabel(), needed);
        } else {
            System.out.println("   Максимальний рівень лояльності!");
        }
    }


    private Customer chooseCustomer(List<Customer> customers) {
        System.out.println("\nОберіть покупця:");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.printf("  %d. %s [%s]%n",
                    i + 1, c.getName(), c.getTier().getLabel());
        }
        int idx = readInt("Номер покупця", 1) - 1;
        idx = Math.max(0, Math.min(idx, customers.size() - 1));
        return customers.get(idx);
    }

    private Product selectProduct(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, products.get(i));
        }
        int idx = readInt("Товар (0 - скасувати)", 0) - 1;
        if (idx < 0 || idx >= products.size()) return null;
        return products.get(idx);
    }

    private String readLine(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt, int defaultValue) {
        try {
            String s = readLine(prompt);
            return s.isEmpty() ? defaultValue : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    private static void printBanner() {
        System.out.println("\n" + "".repeat(57));
        System.out.println("" + " ".repeat(17) + " ELECTROZONE " + " ".repeat(18) + "");
        System.out.println("" + " ".repeat(14) + "Магазин електроніки" + " ".repeat(14) + "");
        System.out.println("".repeat(57));
    }

    private static void printMenu() {
        System.out.println("""

                
                           ГОЛОВНЕ МЕНЮ              
               
                  1. Переглянути каталог             
                  2. Додати товар до кошика          
                  3. Видалити товар з кошика         
                  4. Скасувати останню дію (undo)    
                  5. Переглянути кошик               
                  6. Оформити замовлення             
                  7. Мої замовлення                  
                  8. Порівняти товари                
                  9. Профіль покупця                 
                 10. Статистика                      
                  0. Вийти                           
                """);
    }
}
