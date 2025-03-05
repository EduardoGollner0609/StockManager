package services;

import models.dao.DaoFactory;
import models.dao.SaleDao;
import models.entities.Sale;
import models.entities.SaleItem;

import java.util.ArrayList;
import java.util.List;

public class SaleService {

    private SaleDao saleDao = DaoFactory.createSale();

    private ClientService clientService;

    private SaleItemService saleItemService;

    private CartItemService cartItemService;

    public SaleService(ClientService clientService, SaleItemService saleItemService, CartItemService cartItemService) {
        this.clientService = clientService;
        this.saleItemService = saleItemService;
        this.cartItemService = cartItemService;
    }

    public void insert(Sale sale) {

        clientService.insert(sale.getClient());

        sale = saleDao.insert(sale);

        List<Long> productsIds = new ArrayList<>();

        for (SaleItem saleItem : sale.getItems()) {
            productsIds.add(saleItem.getProduct().getId());
            saleItem.getSale().setId(sale.getId());
            saleItemService.insert(saleItem);
        }

        cartItemService.deleteAllByProductsIds(productsIds);

    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setSaleItemService(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    public void setCartItemService(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

}
