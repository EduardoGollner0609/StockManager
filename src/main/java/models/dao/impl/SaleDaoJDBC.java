package models.dao.impl;

import db.DB;
import db.DbException;
import models.dao.SaleDao;
import models.entities.Sale;

import java.sql.*;

public class SaleDaoJDBC implements SaleDao {

    private Connection conn;

    public SaleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Sale insert(Sale sale) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO tb_sale(client_id, sale_date, total_value, payment_method) " +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, sale.getClient().getId());
            st.setTimestamp(2, java.sql.Timestamp.valueOf(sale.getSaleDate()));
            st.setDouble(3, sale.getTotalValue());
            st.setString(4, sale.getPaymentMethod());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    sale.setId(rs.getLong(1));
                }
            } else {
                throw new DbException("Erro ao inserir Sale");
            }
            return sale;
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir Sale");
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
