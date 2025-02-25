package models.dao;

import services.ProductService;


public class DaoFactory {

    public static ProductDao createProduct() {
        return new ProductService();
    }

}
