package entity;

public class OrderDetail {
    private String orderId;
    private String itemCode;
    private int orderQty;
    private double discount;
    private double price;

    public OrderDetail() {  }

    public OrderDetail(String orderId, String itemCode, int orderQty, double discount, double price) {
        this.setItemCode(itemCode);
        this.setOrderId(orderId);
        this.setOrderQty(orderQty);
        this.setDiscount(discount);
        this.setPrice(price);
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getOrderQty() {
        return orderQty;
    }
    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId='" + orderId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", orderQty=" + orderQty +
                ", discount=" + discount +
                ", price=" + price +
                '}';
    }
}
