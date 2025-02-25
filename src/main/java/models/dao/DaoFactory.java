package models.dao;

import db.DB;
import services.ProductService;


public class DaoFactory {

    public static ProductDao createProduct() {
        return new ProductService(DB.getConnection());
    }

}
