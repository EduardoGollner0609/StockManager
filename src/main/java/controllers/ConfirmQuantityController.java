package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.entities.CartItem;
import models.entities.Product;
import services.CartItemService;
import utils.Alerts;
import utils.Utils;

public class ConfirmQuantityController {

    private Product product;

    private CartItem cartItem;

    private CartItemService cartItemService;

    @FXML
    private Text txtProductName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private Button btnConfirmQuantity;

    @FXML
    public void onBtnConfirmQuantity(ActionEvent event) {

        Integer quantity = convertAndCheckQuantity();

        if (quantity == null) {
            Alerts.showAlert("Erro na quantidade", null, "O campo de quantidade deve receber apenas números e valor maior que zero", Alert.AlertType.ERROR);
            return;
        }

        if (product != null) {
            addProductFromCart(quantity, event);
            return;
        }

        if (cartItem != null) {
            removeProductFromCart(quantity, event);
        }


    }

    private void removeProductFromCart(Integer quantity, ActionEvent event) {
        if (quantity > cartItem.getQuantity()) {
            Alerts.showAlert("Erro ao remover do carrinho", null, "A quantidade exigida é maior que a disponível no carrinho.", Alert.AlertType.ERROR);
        } else {
            cartItemService.removeQuantityFromCart(cartItem.getProductId(), quantity);

            CashierFrontListController.instance.updateTableView();

            StockListController.instance.updateTableView();

            Alerts.showAlert("Sucesso", null, "Removido do carrinho com sucesso! a quantidade de " + quantity, Alert.AlertType.CONFIRMATION);

            Utils.currentStage(event).close();
        }
    }

    private void addProductFromCart(Integer quantity, ActionEvent event) {

        if (quantity > product.getQuantity()) {
            Alerts.showAlert("Erro ao adicionar ao carrinho", null, "A quantidade exigida é maior que a disponível.", Alert.AlertType.ERROR);
        } else {


            cartItemService.addQuantityFromCart(product, quantity);

            CashierFrontListController.instance.updateTableView();

            StockListController.instance.updateTableView();

            StockSearchController.instance.updateTableView();

            Alerts.showAlert("Sucesso", null, "Adicionado ao carrinho com sucesso!", Alert.AlertType.CONFIRMATION);
            Utils.currentStage(event).close();
        }
    }

    private Double calculateTotalValue(Double price, Integer quantity) {
        return price * quantity;
    }

    @FXML
    public Integer convertAndCheckQuantity() {
        try {
            int quantity = Integer.parseInt(txtQuantity.getText());
            if (quantity < 0) {
                return null;
            }
            return quantity;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setProductAndTxtNameProduct(Product product) {
        this.product = product;
        txtProductName.setText("Você está adicionando o produto: " + product.getName());
    }

    public void setCartItemService(CartItemService service) {
        this.cartItemService = service;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}