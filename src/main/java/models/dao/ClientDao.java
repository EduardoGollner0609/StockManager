package models.dao;

import models.entities.Client;

import java.util.List;

public interface ClientDao {

    void insert(Client client);

    Client findById(Long id);

    List<Client> findAll();

    void deleteById(Long id);

    void update(Client client);

}
