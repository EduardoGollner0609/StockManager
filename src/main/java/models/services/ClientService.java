package models.services;

import models.dao.ClientDao;
import models.entities.Client;

import java.util.List;

public class ClientService implements ClientDao {
    @Override
    public void insert(Client client) {

    }

    @Override
    public Client findById(Long id) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(Client client) {

    }
}
