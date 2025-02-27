package models.dao;

import models.entities.CartItem;
import models.entities.Product;

import java.util.List;

public interface CartItemDao {

    void insert(CartItem cartItem);

    List<CartItem> findAll();

    void deleteById(Long id);
}
