package models.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import models.dao.ProductDao;
import models.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private Connection conn;

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product product) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO tb_product" +
                            "(name, description, quantity, price) " +
                            "VALUES " +
                            "(?, ?, ?, ?)");

            prepareStatementInsertData(st, product);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected <= 0) {
                throw new DbException("Não foi possível inserir o produto!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Product findById(Long id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * " +
                            "FROM tb_product " +
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
            rs = st.executeQuery();
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
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM tb_product " +
                    "WHERE id = ?");

            st.setLong(1, id);

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Product product) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE tb_product " +
                    "SET name = ?, description = ?, quantity = ?, price = ? " +
                    "WHERE id = ?");

            prepareStatementInsertData(st, product);
            st.setLong(5, product.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public boolean existsById(Long id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT COUNT(*) " +
                    "FROM tb_product " +
                    "WHERE id = ?");
            st.setLong(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Product instantiateProduct(ResultSet rs) throws SQLException {
        return new Product(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("quantity"),
                rs.getDouble("price")
        );
    }

    private void prepareStatementInsertData(PreparedStatement st, Product product) throws SQLException {
        st.setString(1, product.getName());
        st.setString(2, product.getDescription());
        st.setInt(3, product.getQuantity());
        st.setDouble(4, product.getPrice());
    }
}
