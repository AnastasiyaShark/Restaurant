package Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        List<Waiter> waiters = new ArrayList<>(Arrays.asList(
                new Waiter("Официант 1"),
                new Waiter("Официант 2"),
                new Waiter("Официант 3")
        ));

        List<Visitor> visitors = new ArrayList<>(Arrays.asList(
                new Visitor("Посетитель 1"),
                new Visitor("Посетитель 2"),
                new Visitor("Посетитель 3"),
                new Visitor("Посетитель 4"),
                new Visitor("Посетитель 5")
        ));

        final ExecutorService threadPool = Executors.newFixedThreadPool(6);

        for (Waiter waiter : waiters) {
            threadPool.submit(waiter);
        }
        List<Future<?>> visitorFutureList = new ArrayList<>();
        int tasksDone = 0;
        for (Visitor visitor : visitors) {
            Future<?> visitorFuture = threadPool.submit(visitor);
            visitorFutureList.add(visitorFuture);
        }
        while (tasksDone != visitorFutureList.size()) {
            tasksDone = 0;
            for (Future<?> task : visitorFutureList) {
                if (task.isDone()) {
                    tasksDone++;
                }
            }
        }
        OrdersManager.getInstance().closeRestaurant();
        threadPool.shutdown();
        OrdersManager.getInstance().notifyAboutClosing();
        try {
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
