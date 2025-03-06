package models.entities;

import java.util.Objects;

public class CartItem extends Product {

    private Long productId;
    private Double totalValue;

    public CartItem() {
    }

    public CartItem(Integer quantity, Long productId, Double totalValue) {
        this.quantity = quantity;
        this.productId = productId;
        this.totalValue = totalValue;
    }

    public CartItem(Long id, String name, String description, Integer quantity, Double price, Long productId, Double totalValue) {
        super(id, name, description, quantity, price);
        this.productId = productId;
        this.totalValue = totalValue;
    }

    public CartItem(Long productId, String name, Integer quantity, Double price, Double totalValue) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = totalValue;
    }

    public CartItem(Integer quantity, Double totalValue, Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.quantity = quantity;
        this.price = product.getPrice();
        this.totalValue = totalValue;
    }


    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(productId, cartItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), productId);
    }
}
