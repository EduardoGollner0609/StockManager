package services;

import db.DB;
import db.DbException;
import exceptions.ResourceNotFoundException;
import models.dao.CartItemDao;
import models.dao.DaoFactory;
import models.entities.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemService {

    private CartItemDao cartitemDao = DaoFactory.createCartItem();



    public void updateRemoveQuantity(Long productId, Double price, Integer quantity) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            if (!existsByProductId(st, rs, productId)) {
                throw new ResourceNotFoundException();
            }
            st = conn.prepareStatement(
                    "UPDATE tb_cart " +
                            "SET quantity = quantity - ?, total_value = quantity * ? " +
                            "WHERE product_id = ?");
            st.setInt(1, quantity);
            st.setDouble(2, price);
            st.setLong(3, productId);

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
