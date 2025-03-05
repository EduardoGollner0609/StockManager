package models.dao;

import models.entities.SaleItem;

import java.util.List;

public interface SaleItemDao {

    SaleItem insert(SaleItem saleItem);

    List<SaleItem> findAll();

}
