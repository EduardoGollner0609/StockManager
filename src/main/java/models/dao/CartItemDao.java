package models.dao;

import models.entities.CartItem;
import models.entities.Product;

import java.util.List;

public interface CartItemDao {

    void insert(CartItem cartItem);

    CartItem findByProductId(Long productId);

    List<CartItem> findAll();

    void update(CartItem cartItem);

    void deleteById(Long id);

    boolean existsByProductId(Long productId);


}
