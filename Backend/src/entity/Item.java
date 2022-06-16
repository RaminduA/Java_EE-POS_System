package entity;

public class Item {
    private String itemCode;
    private String description;
    private int qtyOnHand;
    private double unitPrice;
    private double discountPercent;

    public Item() {  }

    public Item(String itemCode, String description, int qtyOnHand, double unitPrice, double discountPercent) {
        this.setItemCode(itemCode);
        this.setDescription(description);
        this.setQtyOnHand(qtyOnHand);
        this.setUnitPrice(unitPrice);
        this.setDiscountPercent(discountPercent);
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }
    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemCode='" + getItemCode() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qtyOnHand=" + getQtyOnHand() +
                ", unitPrice=" + getUnitPrice() +
                ", discountPercent=" + getDiscountPercent() +
                '}';
    }
}
