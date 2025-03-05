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
    public void insert(Client client) {
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
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Client findByName(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * " +
                            "FROM tb_client " +
                            "WHERE name = ?");
            st.setString(1, name);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateClient(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Client instantiateClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("phone")
        );
    }
}
