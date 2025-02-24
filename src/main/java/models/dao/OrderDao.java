package models.dao;

import models.entities.Order;
import models.entities.Product;

import java.util.List;

public interface OrderDao {

    void insert(Order order);

    Order findById(Long id);

    List<Order> findAll();

    void deleteById(Long id);

    void updateById(Long id);

}
