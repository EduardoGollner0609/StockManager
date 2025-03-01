package models.dao;

import db.DB;
import models.dao.impl.CartItemDaoJDBC;
import models.dao.impl.ProductDaoJDBC;

public class DaoFactory {

    public static ProductDao createProduct() {
        return new ProductDaoJDBC(DB.getConnection());
    }

    public static CartItemDao createCartItem() {
        return new CartItemDaoJDBC(DB.getConnection());
    }
}
