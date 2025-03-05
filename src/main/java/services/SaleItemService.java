package services;

import models.dao.DaoFactory;
import models.dao.SaleItemDao;
import models.entities.SaleItem;

import java.util.List;

public class SaleItemService {

    private SaleItemDao saleItemDao = DaoFactory.createSaleItem();

    public SaleItem insert(SaleItem saleItem) {
        return saleItemDao.insert(saleItem);
    }

    public List<SaleItem> findAll() {
        return saleItemDao.findAll();
    }

}
