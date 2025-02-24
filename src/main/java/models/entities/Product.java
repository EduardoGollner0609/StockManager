package models.entities;

import java.util.Objects;

public class Product {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String mark;
    private Integer quantity;
    private Double price;
    private String measuremenetUnit;

    public Product() {
    }

    public Product(Long id, String name, String description, String category, String mark,
                   Integer quantity, Double price, String measuremenetUnit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.mark = mark;
        this.quantity = quantity;
        this.price = price;
        this.measuremenetUnit = measuremenetUnit;
    }

    public String getMeasuremenetUnit() {
        return measuremenetUnit;
    }

    public void setMeasuremenetUnit(String measuremenetUnit) {
        this.measuremenetUnit = measuremenetUnit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
