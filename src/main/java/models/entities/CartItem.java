package models.entities;

public class CartItem extends Product {

    private Long productId;
    private Double totalValue;

    public CartItem() {

    }

    public CartItem(Long id, String name, String description, Integer quantity, Double price, Long productId, Double totalValue) {
        super(id, name, description, quantity, price);
        this.productId = productId;
        this.totalValue = totalValue;
    }

    public CartItem(Long id, String name, Integer quantity, Double price, Double totalValue) {
        this.id = id;
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
}
