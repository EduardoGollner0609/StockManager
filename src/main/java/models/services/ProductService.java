package models.services;

import models.dao.ProductDao;
import models.entities.Product;

import java.util.List;

public class ProductService implements ProductDao {

    @Override
    public void insert(Product product) {

    }

    @Override
    public Product findById(Long id) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void updateById(Long id) {

    }

}
