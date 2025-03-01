package services;

import exceptions.ResourceNotFoundException;
import models.dao.CartItemDao;
import models.dao.DaoFactory;
import models.entities.CartItem;

import java.util.List;

public class CartItemService {

    private final CartItemDao cartItemDao = DaoFactory.createCartItem();

    private ProductService productService;

    public List<CartItem> findAll() {
        return cartItemDao.findAll();
    }

    public void removeQuantityFromCart(Long productId, Integer quantity) {

        setProductService(new ProductService());

        CartItem cartItem = cartItemDao.findByProductId(productId);


        if (cartItem == null) {
            throw new ResourceNotFoundException();
        }

        if (cartItem.getQuantity() <= quantity) {
            cartItemDao.deleteById(cartItem.getId());
            productService.updateSumQuantity(productId, cartItem.getQuantity());
        }

        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        cartItem.setTotalValue(cartItem.getPrice() * cartItem.getQuantity());

        cartItemDao.update(cartItem);

        productService.updateSumQuantity(productId, quantity);


    }

    public void setProductService(ProductService service) {
        this.productService = service;
    }
}
