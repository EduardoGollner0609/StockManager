package models.services;

import models.dao.OrderDao;
import models.entities.Order;

import java.util.List;

public class OrderService implements OrderDao {


    @Override
    public void insert(Order order) {

    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(Order order) {

    }
}
