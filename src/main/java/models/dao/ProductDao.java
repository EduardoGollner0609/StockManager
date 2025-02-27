package models.dao;

import models.entities.Product;

import java.util.List;

public interface ProductDao {

    void insert(Product product);

    Product findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    void update(Product product);

    void saveOrUpdate(Product product);
}
