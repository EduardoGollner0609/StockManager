package models.services;

import db.DbException;
import models.dao.ProductDao;
import models.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductService implements ProductDao {

    private Connection conn;

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
                Product product = new Product(rs.getLong("id"), rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("mark"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("measuremenet_unit"));
                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void updateById(Long id) {

    }

}
