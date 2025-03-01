package services;

import db.DB;
import db.DbException;
import models.dao.DaoFactory;
import models.dao.ProductDao;
import models.entities.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductService {

    private ProductDao productDao = DaoFactory.createProduct();

    public void saveOrUpdate(Product product) {
        if (product.getId() == null) {
            productDao.insert(product);
        } else {
            productDao.update(product);
        }
    }

    public void updateSumQuantity(Long id, Integer quantity) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE tb_product " +
                    "SET quantity = quantity + ? " +
                    "WHERE id = ?");
            st.setInt(1, quantity);
            st.setLong(2, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}