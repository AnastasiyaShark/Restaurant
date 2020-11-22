package Restaurant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OrdersManager {
    private static OrdersManager instance = null;

    private static final int DEFAULT_WAIT_TIME = 3000;
    private final Queue<Order> incomingOrders;
    private final List<Order> finishedOrders;
    private volatile boolean isOpen;


    public static synchronized OrdersManager getInstance() {
        if (instance == null) {
            instance = new OrdersManager();
        }
        return instance;
    }

    private OrdersManager() {
        incomingOrders = new LinkedList<>();
        finishedOrders = new ArrayList<>();
        isOpen = true;
    }

    public boolean isOpen() {
        synchronized (instance) {
            return isOpen;
        }
    }

    public void placeIncomingOrder(Order order) {
        synchronized (instance) {
            incomingOrders.add(order);
            notifyAll();
        }
    }

    public void waitForIncomingOrder() throws InterruptedException {
        synchronized (instance) {
            if (incomingOrders.size() == 0) {
                wait(DEFAULT_WAIT_TIME);
            }
        }
    }

    public Order getIncomingOrder() {
        synchronized (instance) {
            return incomingOrders.poll();
        }
    }

    public Order getFinishedOrderBy(String orderName) {
        synchronized (instance) {
            for (int i = 0; i < finishedOrders.size(); i++) {
                Order finishedOrder = finishedOrders.get(i);
                if (finishedOrder.getName().equals(orderName)) {
                    finishedOrders.remove(i);
                    return finishedOrder;
                }
            }
        }
        return null;
    }

    public void placeFinishedOrder(Order order) {
        synchronized (instance) {
            finishedOrders.add(order);
            notifyAll();
        }
    }

    public void waitForFinishedOrder() throws InterruptedException {
        synchronized (instance) {
            if (finishedOrders.size() == 0) {
                wait(DEFAULT_WAIT_TIME);
            }
        }
    }

    public void closeRestaurant() {
        isOpen = false;
    }

    public void notifyAboutClosing() {
        synchronized (instance) {
            notifyAll();
        }
    }
}
