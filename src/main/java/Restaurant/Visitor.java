package Restaurant;


public class Visitor implements Runnable {
    private final String myName;

    public Visitor(String name) {
        this.myName = name;
    }


    @Override
    public void run() {
        try {
            Thread.currentThread().setName(myName);
            OrdersManager ordersManager = OrdersManager.getInstance();
            System.out.printf("%s в ресторане %n", myName);
            int timeToThink = 2000;
            Thread.sleep(timeToThink);
            String orderName;
            {
                Order myOrder = new Order(myName);
                orderName = myOrder.getName();
                System.out.printf("%s делает заказ %n", myName);
                ordersManager.placeIncomingOrder(myOrder);
            }
            Order myFinishedOrder = null;
            while ((myFinishedOrder = ordersManager.getFinishedOrderBy(orderName)) == null) {
                System.out.printf("%s ждёт свой заказ :(%n", myName);
                ordersManager.waitForFinishedOrder();
            }
            System.out.printf("%s получил свой заказ (%s) %n", myName, myFinishedOrder.getName());

            System.out.printf("%s Приступил к еде%n", myName);
            Thread.sleep(timeToThink);
            System.out.printf("%s Вышел из ресторана%n", myName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
