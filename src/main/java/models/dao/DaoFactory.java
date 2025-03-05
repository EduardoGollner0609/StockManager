package models.dao;

import db.DB;
import models.dao.impl.*;

public class DaoFactory {

    public static ProductDao createProduct() {
        return new ProductDaoJDBC(DB.getConnection());
    }

    public static CartItemDao createCartItem() {
        return new CartItemDaoJDBC(DB.getConnection());
    }

    public static ClientDao createClient() {
        return new ClientDaoJDBC(DB.getConnection());
    }

    public static SaleDao createSale() {
        return new SaleDaoJDBC(DB.getConnection());
    }

    public static SaleItemDao createSaleItem() {
        return new SaleItemDaoJDBC(DB.getConnection());
    }

}
