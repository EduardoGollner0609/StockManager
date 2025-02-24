package models.dao;

import models.services.ClientService;
import models.services.OrderService;
import models.services.ProductService;


public class DaoFactory {

    public static ProductDao createProduct() {
        return new ProductService();
    }

    public static OrderDao createOrder() {
        return new OrderService();
    }

    public static ClientDao createClient() {
        return new ClientService();
    }
}
