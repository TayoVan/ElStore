package store.singleton;

import store.model.Customer;

import java.util.*;


public class CustomerRepository {

    private static CustomerRepository instance;

    private CustomerRepository() {}

    public static synchronized CustomerRepository getInstance() {
        if (instance == null) instance = new CustomerRepository();
        return instance;
    }

    // Сховище
    private final Map<String, Customer> customers = new LinkedHashMap<>();

    public void add(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public Customer get(String customerId) {
        return customers.get(customerId);
    }

    public List<Customer> findAll() {
        return List.copyOf(customers.values());
    }
}
