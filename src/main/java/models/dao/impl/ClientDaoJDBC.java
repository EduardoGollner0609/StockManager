package models.dao.impl;

import db.DB;
import db.DbException;
import models.dao.ClientDao;
import models.entities.Client;

import java.sql.*;

public class ClientDaoJDBC implements ClientDao {

    private Connection conn;

    public ClientDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Client insert(Client client) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {

            st = conn.prepareStatement(
                    "INSERT INTO tb_client(name, phone) " +
                            "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, client.getName());
            st.setString(2, client.getPhone());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    client.setId(rs.getLong(1));
                }
            } else {
                throw new DbException("Erro ao inserir cliente");
            }
            return client;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
