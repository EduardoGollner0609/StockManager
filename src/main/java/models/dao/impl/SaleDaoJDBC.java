package models.dao.impl;

import db.DB;
import db.DbException;
import models.dao.SaleDao;
import models.entities.Sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaleDaoJDBC implements SaleDao {

    private Connection conn;

    public SaleDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Sale sale) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO tb_sale(client_id, sale_date, total_value) " +
                            "VALUES (?, ?, ?)");
            st.setLong(1, sale.getClient().getId());
            st.setTimestamp(2, java.sql.Timestamp.valueOf(sale.getSaleDate()));
            st.setDouble(3, sale.getTotalValue());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected <= 0) {
                throw new DbException("Erro ao inserir Sale");
            }

        } catch (SQLException e) {
            throw new DbException("Erro ao inserir Sale");
        } finally {
            DB.closeStatement(st);
        }
    }
}
