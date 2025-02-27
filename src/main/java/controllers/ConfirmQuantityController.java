package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import listeners.DataChangeListener;
import models.dao.CartItemDao;
import models.dao.DaoFactory;
import models.dao.ProductDao;
import models.entities.CartItem;
import models.entities.Product;
import utils.Alerts;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConfirmQuantityController {

    private Product product;

    @FXML
    private Text txtProductName;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtQuantity;

    @FXML
    private Button btnConfirmQuantity;

    @FXML
    public void onBtnConfirmQuantity(ActionEvent event) {

        Integer quantity = convertAndCheckQuantity();

        if (quantity == null) {
            Alerts.showAlert("Erro na quantidade", null, "O campo de quantidade deve receber apenas números e valor maior que 0", Alert.AlertType.ERROR);
            return;
        }

        if (product == null) {
            return;
        }

        confirmOperationCart(quantity, event);
    }


    private void confirmOperationCart(Integer quantity, ActionEvent event) {

        if (quantity != null && quantity > product.getQuantity()) {
            Alerts.showAlert("Erro ao adicionar ao carrinho", null, "A quantidade exigida é maior que a disponível.", Alert.AlertType.ERROR);
        } else {
            CartItemDao cartItemDao = DaoFactory.createCartItem();
            cartItemDao.insert(new CartItem(quantity, calculateTotalValue(product.getPrice(), quantity), product));
            product.setQuantity(product.getQuantity() - quantity);
            ProductDao productDao = DaoFactory.createProduct();
            productDao.update(product);

            if (CashierFrontListController.instance != null) {
                CashierFrontListController.instance.updateTableView();
            }

            if (StockListController.instance != null) {
                StockListController.instance.updateTableView();
            }

            notifyDataChangeListeners();

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

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public void setProductAndTxtNameProduct(Product product) {
        this.product = product;
        txtProductName.setText("Você está adicionando o produto: " + product.getName());
    }
}