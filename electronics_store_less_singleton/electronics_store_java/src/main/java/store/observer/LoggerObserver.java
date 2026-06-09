package store.observer;


public class LoggerObserver implements StoreObserver {

    private String[] trackedTypes;


    public LoggerObserver(String... trackedTypes) {
        this.trackedTypes = trackedTypes;
    }

    @Override
    public void onEvent(Event event) {
        if (trackedTypes.length == 0 || matches(event.getType())) {
            System.out.printf("[LOG %s] %s %s%n",
                    event.getTimestamp(), event.getType(), event.getData());
        }
    }

    public boolean matches(String type) {
        for (var t : trackedTypes) {
            if (t.equals(type)) return true;
        }
        return false;
    }
}
