package models.dao.impl;

import db.DB;
import db.DbException;
import models.dao.SaleItemDao;
import models.entities.Client;
import models.entities.Product;
import models.entities.Sale;
import models.entities.SaleItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<SaleItem> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT si.id, si.quantity, si.price, si.total_value, si.product_id, si.sale_id, " +
                            "pt.name AS product_name, pt.description, " +
                            "sl.client_id, sl.sale_date, sl.total_value AS sale_total_value, sl.payment_method, sl.observation, " +
                            "cl.name AS client_name, cl.phone AS client_phone, cl.cpf AS client_cpf " +
                            "FROM tb_sale_item si " +
                            "INNER JOIN tb_product pt ON pt.id = si.product_id " +
                            "INNER JOIN tb_sale sl ON sl.id = si.sale_id " +
                            "INNER JOIN tb_client cl ON cl.id = sl.client_id");
            rs = st.executeQuery();

            List<SaleItem> items = new ArrayList<>();

            while (rs.next()) {
                items.add(instantiateSaleItem(rs));
            }

            return items;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private SaleItem instantiateSaleItem(ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getLong("client_id"),
                rs.getString("client_name"),
                rs.getString("client_cpf"),
                rs.getString("client_phone")
        );

        Sale sale = new Sale(
                rs.getLong("sale_id"), client,
                rs.getTimestamp("sale_date").toLocalDateTime(),
                rs.getDouble("sale_total_value"),
                rs.getString("payment_method"),
                rs.getString("observation")
                );

        Product product = new Product(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getString("description"),
                rs.getInt("quantity"),
                rs.getDouble("price")
        );


        return new SaleItem(
                rs.getLong("id"),
                sale,
                product,
                rs.getInt("quantity"),
                rs.getDouble("price"),
                rs.getDouble("total_value")
        );
    }
}
