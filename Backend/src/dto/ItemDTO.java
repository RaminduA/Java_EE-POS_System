package dto;

public class ItemDTO {
    private String itemCode;
    private String name;
    private double unitPrice;
    private int qtyOnHand;

    public ItemDTO() {  }

    public ItemDTO(String itemCode, String name, double unitPrice, int qtyOnHand) {
        this.setItemCode(itemCode);
        this.setName(name);
        this.setUnitPrice(unitPrice);
        this.setQtyOnHand(qtyOnHand);
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }
    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemCode='" + getItemCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", unitPrice=" + getUnitPrice() +
                ", qtyOnHand=" + getQtyOnHand() +
                '}';
    }
}
