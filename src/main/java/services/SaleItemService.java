package services;

import models.dao.DaoFactory;
import models.dao.SaleItemDao;
import models.entities.SaleItem;

public class SaleItemService {

    private SaleItemDao saleItemDao = DaoFactory.createSaleItem();

    public SaleItem insert(SaleItem saleItem) {
        return saleItemDao.insert(saleItem);
    }

}
