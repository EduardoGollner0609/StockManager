package services;

import exceptions.ResourceNotFoundException;
import models.dao.DaoFactory;
import models.dao.ProductDao;
import models.entities.Product;

import java.util.List;

public class ProductService {

    private ProductDao productDao = DaoFactory.createProduct();

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public void deleteById(Long id) {
        if (!productDao.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        productDao.deleteById(id);
    }

    public void saveOrUpdate(Product product) {
        if (product.getId() == null) {
            productDao.insert(product);
        } else {
            productDao.update(product);
        }
    }

    public void updateSumQuantity(Long id, Integer quantity) {
        Product product = productDao.findById(id);

        if (product == null) {
            throw new ResourceNotFoundException();
        }

        product.setQuantity(product.getQuantity() + quantity);
        productDao.update(product);
    }

    public void updateRemoveQuantity(Long id, Integer quantity) {
        Product product = productDao.findById(id);

        if (product == null) {
            throw new ResourceNotFoundException();
        }

        int newQuantity = product.getQuantity() - quantity;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantidade maior que a disponivel");
        }

        product.setQuantity(newQuantity);
        productDao.update(product);
    }
}