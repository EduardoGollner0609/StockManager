package services;

import models.dao.DaoFactory;
import models.dao.SaleDao;
import models.entities.Sale;
import models.entities.SaleItem;

public class SaleService {

    private SaleDao saleDao = DaoFactory.createSale();

    private ClientService clientService;

    private SaleItemService saleItemService;

    public SaleService(ClientService clientService, SaleItemService saleItemService) {
        this.clientService = clientService;
        this.saleItemService = saleItemService;
    }

    public void insert(Sale sale) {
        clientService.insert(sale.getClient());
        saleDao.insert(sale);
        for (SaleItem saleItem : sale.getItems()) {
            saleItemService.insert(saleItem);
        }
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setSaleItemService(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

}
