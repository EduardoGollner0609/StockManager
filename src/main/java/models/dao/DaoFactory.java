package models.dao;

import db.DB;
import services.CartItemService;
import services.ProductService;


public class DaoFactory {

    public static ProductDao createProduct() {
        return new ProductService(DB.getConnection());
    }

    public static CartItemDao createCartItem() {
        return new CartItemService(DB.getConnection());
    }

}
