package models.dao.impl;

import db.DB;
import db.DbException;
import exceptions.ResourceNotFoundException;
import models.dao.CartItemDao;
import models.entities.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            if (existsByProductId(st, rs, cartItem.getProductId())) {
                st = conn.prepareStatement("UPDATE tb_cart " +
                        "SET quantity = quantity + ? " +
                        "WHERE product_id = ?");
                st.setInt(1, cartItem.getQuantity());
                st.setLong(2, cartItem.getProductId());

                int rowsAffected = st.executeUpdate();

                if (rowsAffected <= 0) {
                    throw new DbException("Não foi possível inserir o produto!");
                }
            } else {
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
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
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
    public void deleteById(Long id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "DELETE FROM tb_cart " +
                            "WHERE id = ?");
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }


    private boolean existsByProductId(PreparedStatement st, ResultSet rs, Long productId) throws SQLException {
        st = conn.prepareStatement("SELECT COUNT(*) " +
                "FROM tb_cart " +
                "WHERE product_id = ?");
        st.setLong(1, productId);
        rs = st.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    private CartItem instantiateCartItem(ResultSet rs) throws SQLException {
        return new CartItem(rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getDouble("price"),
                rs.getDouble("total_value"));
    }

}
