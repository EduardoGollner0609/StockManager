package models.dao;

import models.entities.Client;

public interface ClientDao {

    void insert(Client client);

    Client findByName(String name);

}
