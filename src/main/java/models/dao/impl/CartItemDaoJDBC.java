package models.dao.impl;

import db.DB;
import db.DbException;
import models.dao.CartItemDao;
import models.entities.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartItemDaoJDBC implements CartItemDao {

    private Connection conn;

    public CartItemDaoJDBC() {
    }

    public CartItemDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(CartItem cartItem) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO tb_cart(quantity, total_value, product_id) " +
                            "VALUES (?, ?, ?)");

            st.setInt(1, cartItem.getQuantity());
            st.setDouble(2, cartItem.getTotalValue());
            st.setLong(3, cartItem.getProductId());

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
    public CartItem findByProductId(Long productId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT pt.id, pt.name, pt.price, ct.quantity, ct.total_value " +
                    "FROM tb_cart ct " +
                    "INNER JOIN tb_product pt " +
                    "ON pt.id = ct.product_id " +
                    "WHERE pt.id = ?");
            st.setLong(1, productId);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateCartItem(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<CartItem> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT pt.id, pt.name, pt.price, ct.quantity, ct.total_value " +
                            "FROM tb_cart ct " +
                            "INNER JOIN tb_product pt " +
                            "ON pt.id = ct.product_id");

            rs = st.executeQuery();

            List<CartItem> cart = new ArrayList<>();

            while (rs.next()) {
                cart.add(instantiateCartItem(rs));
            }
            return cart;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(CartItem cartItem) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE tb_cart " +
                    "SET quantity = ?, total_value = ? " +
                    "WHERE product_id = ?");
            st.setInt(1, cartItem.getQuantity());
            st.setDouble(2, cartItem.getTotalValue());
            st.setLong(3, cartItem.getProductId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByProductId(Long id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "DELETE FROM tb_cart " +
                            "WHERE product_id = ?");
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteAllByProductsIds(List<Long> productsIds) {
        if (productsIds == null || productsIds.isEmpty()) {
            return;
        }

        PreparedStatement st = null;
        try {
            String placeholders = String.join(",", Collections.nCopies(productsIds.size(), "?"));
            st = conn.prepareStatement("DELETE FROM tb_cart WHERE product_id IN (" + placeholders + ")");

            for (int i = 0; i < productsIds.size(); i++) {
                st.setLong(i + 1, productsIds.get(i));
            }

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    public boolean existsByProductId(Long productId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT COUNT(*) " +
                    "FROM tb_cart " +
                    "WHERE product_id = ?");
            st.setLong(1, productId);
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

    private CartItem instantiateCartItem(ResultSet rs) throws SQLException {
        return new CartItem(rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getDouble("price"),
                rs.getDouble("total_value"));
    }

}
