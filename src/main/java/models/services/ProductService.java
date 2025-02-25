package models.services;

import db.DB;
import db.DbException;
import models.dao.ProductDao;
import models.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements ProductDao {

    private Connection conn;

    public ProductService() {
    }

    public ProductService(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product product) {
      
    }

    @Override
    public Product findById(Long id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT *" +
                            "FROM tb_product" +
                            "WHERE id = ?"
            );
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateProduct(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT *" +
                    "FROM tb_product");
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(instantiateProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void updateById(Long id) {

    }

    private Product instantiateProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("quantity"),
                rs.getDouble("price")
        );
    }
}
