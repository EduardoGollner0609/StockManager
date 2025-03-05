package models.dao.impl;

import db.DB;
import db.DbException;
import models.dao.SaleItemDao;
import models.entities.SaleItem;

import java.sql.*;

public class SaleItemDaoJDBC implements SaleItemDao {

    private Connection conn;

    public SaleItemDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public SaleItem insert(SaleItem saleItem) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO tb_sale_item(sale_id, product_id, quantity, price, total_value)" +
                            " VALUES(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setLong(1, saleItem.getSale().getId());
            st.setLong(2, saleItem.getProduct().getId());
            st.setInt(3, saleItem.getQuantity());
            st.setDouble(4, saleItem.getPrice());
            st.setDouble(5, saleItem.getTotalValue());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    saleItem.setId(rs.getLong(1));
                }
            } else {
                throw new DbException("Erro ao inserir");
            }

            return saleItem;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
