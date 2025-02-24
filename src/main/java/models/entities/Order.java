package models.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {

    private Long id;
    private Instant moment;
    private Set<Product> products = new HashSet<>();
    private Client client;

    public Order() {
    }

    public Order(Client client, Product product, Instant moment, Long id) {
        this.client = client;
        this.moment = moment;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
