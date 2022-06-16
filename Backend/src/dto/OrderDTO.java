package dto;

import java.util.ArrayList;

public class OrderDTO{
	private String orderId;
    private String customerId;
    private String orderDate;
    private String orderTime;
    private double cost;
    private ArrayList<OrderDetailDTO> detailList;

    public OrderDTO() {   }

    public OrderDTO(String orderId, String customerId, String orderDate, String orderTime, double cost, ArrayList<OrderDetailDTO> detailList) {
        this.setOrderId(orderId);
        this.setCustomerId(customerId);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
        this.setCost(cost);
        this.setDetailList(detailList);
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

    public ArrayList<OrderDetailDTO> getDetailList() {
        return detailList;
    }
    public void setDetailList(ArrayList<OrderDetailDTO> detailList) {
        this.detailList = detailList;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + getOrderId() + '\'' +
                ", customerId='" + getCustomerId() + '\'' +
                ", orderDate='" + getOrderDate() + '\'' +
                ", orderTime='" + getOrderTime() + '\'' +
                ", cost=" + getCost() +
                '}';
    }
}
