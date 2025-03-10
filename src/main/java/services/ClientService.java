package services;

import exceptions.ResourceNotFoundException;
import models.dao.ClientDao;
import models.dao.DaoFactory;
import models.entities.Client;

public class ClientService {

    private ClientDao clientDao = DaoFactory.createClient();

    public void insert(Client client) {

        Client obj = clientDao.findByCpf(client.getCpf());

        if (obj == null) {
            if(client.getName().isEmpty() && client.getPhone().isEmpty()) {
                throw new ResourceNotFoundException("Cliente não encontrado");
            }
            clientDao.insert(client);
        }

        if(obj != null) {
            client.setId(obj.getId());
        }
    }

}
