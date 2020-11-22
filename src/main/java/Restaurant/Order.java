package Restaurant;

public class Order {
    private String name;

    public Order(String name) {
        this.name = String.format("Order from %s", name);
    }

    public String getName() {
        return name;
    }
}
