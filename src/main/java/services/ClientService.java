package services;

import models.dao.ClientDao;
import models.dao.DaoFactory;
import models.entities.Client;

public class ClientService {

    private ClientDao clientDao = DaoFactory.createClient();

    public void insert(Client client) {

        Client obj = clientDao.findByName(client.getName());

        if (obj == null) {
            clientDao.insert(client);
        } else {
            client.setId(obj.getId());
        }
    }

}
