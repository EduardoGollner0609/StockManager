package models.entities;

public class CartItem extends Product {

    private Double totalValue;

    public CartItem() {

    }

    public CartItem(Long id, String name, String description, Integer quantity, Double price, Double totalValue) {
        super(id, name, description, quantity, price);
        this.totalValue = totalValue;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

}
