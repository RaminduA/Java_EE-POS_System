package entity;

public class Order{
	private String orderId;
    private String customerId;
    private String orderDate;
    private String orderTime;
    private double cost;

    public Order() {   }

    public Order(String orderId, String customerId, String orderDate, String orderTime, double cost) {
        this.setOrderId(orderId);
        this.setCustomerId(customerId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCost(cost);
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + getOrderId() + '\'' +
                ", customerId='" + getCustomerId() + '\'' +
                ", orderDate='" + getOrderDate() + '\'' +
                ", orderTime='" + getOrderTime() + '\'' +
                ", cost=" + getCost() +
                '}';
    }
}
