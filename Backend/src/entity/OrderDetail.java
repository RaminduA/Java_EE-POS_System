package entity;

public class OrderDetail {
    private String orderId;
    private String itemCode;
    private double unitPrice;
    private int orderQty;
    private double price;

    public OrderDetail() {  }

    public OrderDetail(String orderId, String itemCode, double unitPrice, int orderQty, double price) {
        this.setItemCode(itemCode);
        this.setOrderId(orderId);
        this.setUnitPrice(unitPrice);
        this.setOrderQty(orderQty);
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

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getOrderQty() {
        return orderQty;
    }
    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
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
                ", unitPrice=" + unitPrice +
                ", orderQty=" + orderQty +
                ", price=" + price +
                '}';
    }
}
