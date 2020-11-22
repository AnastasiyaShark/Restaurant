package Restaurant;

public class Waiter implements Runnable {
    private final String myName;

    public Waiter(String name) {
        this.myName = name;
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setName(myName);
            OrdersManager ordersManager = OrdersManager.getInstance();
            while (Thread.currentThread().isAlive() && !Thread.currentThread().isInterrupted()) {
                System.out.printf("%s На работе!%n", myName);

                Order incomingOrder = null;
                while ((incomingOrder = ordersManager.getIncomingOrder()) == null) {
                    if (!ordersManager.isOpen()) {
                        System.out.printf("%s Завершает свою работу и уходит...%n", myName);
                        return;
                    }
                    System.out.printf("%s ожидает заказа%n", myName);
                    ordersManager.waitForIncomingOrder();
                }
                System.out.printf("%s взял заказ %s %n", myName, incomingOrder.getName());
                System.out.println("Повар готовит блюдо");
                int timeToPrepare = 2000;
                Thread.sleep(timeToPrepare);
                System.out.println("Повар закончил готовить");
                System.out.printf("%s несет заказ %n", myName);
                ordersManager.placeFinishedOrder(incomingOrder);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
