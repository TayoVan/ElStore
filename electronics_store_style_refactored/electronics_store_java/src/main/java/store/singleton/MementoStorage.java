package store.singleton;

import store.memento.CartMemento;

import java.util.*;


public class MementoStorage {

    //Singleton
    private static MementoStorage instance;

    private MementoStorage() {}

    public static synchronized MementoStorage getInstance() {
        if (instance == null) instance = new MementoStorage();
        return instance;
    }

    // Сховище

    private Map<String, Deque<CartMemento>> history = new HashMap<>();

    public void save(String customerId, CartMemento memento) {
        history.computeIfAbsent(customerId, k -> new ArrayDeque<>()).push(memento);
    }

    public CartMemento restore(String customerId) {
        Deque<CartMemento> stack = history.get(customerId);
        if (stack == null || stack.size() < 2) return null;
        stack.pop();               // видаляємо поточний
        return stack.peek();       // повертаємо попередній (не видаляємо)
    }

    public int historySize(String customerId) {
        Deque<CartMemento> stack = history.get(customerId);
        return (stack == null) ? 0 : stack.size();
    }
}
