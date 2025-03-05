package models.entities;

import java.util.Objects;

public class SaleItem {

    private Long id;
    private Sale sale;
    private Product product;
    private Integer quantity;
    private Double price;
    private Double totalValue;

    public SaleItem() {
    }

    public SaleItem(Long id, Sale sale, Product product, Integer quantity, Double price, Double totalValue) {
        this.id = id;
        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = totalValue;
    }

    public SaleItem(CartItem cartItem) {
        this.product = new Product(cartItem.getProductId(), cartItem.getName(), cartItem.getDescription(), cartItem.getQuantity(), cartItem.getPrice());
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
        this.totalValue = cartItem.getTotalValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return Objects.equals(id, saleItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
