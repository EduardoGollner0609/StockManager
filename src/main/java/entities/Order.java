package entities;

import java.time.Instant;

public class Order {

    private Long id;
    private Instant moment;
    private Product product;
    private Client client;

    public Order() {
    }

    public Order(Client client, Product product, Instant moment, Long id) {
        this.client = client;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
