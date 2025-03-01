package services;

import exceptions.ResourceNotFoundException;
import models.dao.CartItemDao;
import models.dao.DaoFactory;
import models.entities.CartItem;
import models.entities.Product;

import java.util.List;

public class CartItemService {

    private CartItemDao cartItemDao = DaoFactory.createCartItem();

    private ProductService productService;

    public CartItemService(ProductService productService) {
        this.productService = productService;
    }

    public List<CartItem> findAll() {
        return cartItemDao.findAll();
    }

    public void removeQuantityFromCart(Long productId, Integer quantity) {

        CartItem cartItem = cartItemDao.findByProductId(productId);


        if (cartItem == null) {
            throw new ResourceNotFoundException("Não encontrado");
        }

        if (cartItem.getQuantity() <= quantity) {
            cartItemDao.deleteByProductId(cartItem.getProductId());
            productService.updateSumQuantity(productId, cartItem.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cartItem.setTotalValue(cartItem.getPrice() * cartItem.getQuantity());

            cartItemDao.update(cartItem);

            productService.updateSumQuantity(productId, quantity);

        }
    }

    public void addQuantityFromCart(Product product, Integer quantity) {

        CartItem cartItem = cartItemDao.findByProductId(product.getId());

        productService.updateRemoveQuantity(product.getId(), quantity);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalValue(cartItem.getPrice() * cartItem.getQuantity());
            cartItemDao.update(cartItem);

        } else {
            cartItemDao.insert(new CartItem(quantity, product.getId(), quantity * product.getPrice()));
        }
    }

}
