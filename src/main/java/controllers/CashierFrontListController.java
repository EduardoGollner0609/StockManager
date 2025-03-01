package controllers;

import exceptions.CaracterInvalidException;
import exceptions.FieldRequiredNullException;
import exceptions.ResourceNotFoundException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.DataChangeListener;
import models.dao.DaoFactory;
import models.dao.ProductDao;
import models.entities.CartItem;
import models.entities.Product;
import services.CartItemService;
import utils.Alerts;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CashierFrontListController implements Initializable, DataChangeListener {


    public static CashierFrontListController instance;

    private CartItemService service;

    @FXML
    private TextField txtProductId;

    @FXML
    private TextField txtQuantity;

    @FXML
    private Button btnRemove;

    @FXML
    private TableView<CartItem> tableViewCart;

    @FXML
    private TableColumn<CartItem, Integer> tableColumnId;

    @FXML
    private TableColumn<CartItem, String> tableColumnName;

    @FXML
    private TableColumn<CartItem, String> tableColumnDescription;

    @FXML
    private TableColumn<CartItem, Integer> tableColumnQuantity;

    @FXML
    private TableColumn<CartItem, String> tableColumnPrice;

    @FXML
    private TableColumn<CartItem, String> tableColumnTotalValue;

    private ObservableList<CartItem> cartItemList;

    @FXML
    private Button btnOpenStockSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {

        instance = this;

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableColumnPrice.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("R$ %.2f", data.getValue().getPrice()
                )));
        tableColumnTotalValue.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("R$ %.2f", data.getValue().getTotalValue()
                )));

        setCartItemService(new CartItemService());

        updateTableView();
    }

    public void updateTableView() {

        if (service == null) {
            throw new IllegalArgumentException("Service é nulo");
        }

        List<CartItem> list = service.findAll();

        cartItemList = FXCollections.observableArrayList(list);
        tableViewCart.setItems(cartItemList);
    }


    @FXML
    public void onBtnOpenStockSearch() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/stockmanager/gui/stock-search.fxml"));
        Pane pane = loader.load();

        Stage stage = new Stage();

        stage.setScene(new Scene(pane));
        stage.setTitle("StockManager");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.show();
    }


    @FXML
    public void onBtnRemove() {
        try {
            validateFormRemove();

            Integer quantity = Integer.parseInt(txtQuantity.getText());
            Long productId = Long.parseLong(txtProductId.getText());

            service.removeQuantityFromCart(productId, quantity);

            updateTableView();

            StockListController.instance.updateTableView();

        } catch (FieldRequiredNullException e) {
            Alerts.showAlert(
                    "Erro ao remover quantidade",
                    null,
                    "Os campos de código do produto e quantidade não podem ficar vazios.",
                    Alert.AlertType.ERROR);
        } catch (CaracterInvalidException e) {
            Alerts.showAlert(
                    "Erro ao remover quantidade",
                    null,
                    "Os campos de código do produto e quantidade não podem receber letras ou caracteres especiais.",
                    Alert.AlertType.ERROR);
        } catch (ResourceNotFoundException e) {
            Alerts.showAlert(
                    "Erro ao remover quantidade",
                    null,
                    "Não foi encontrado o produto.",
                    Alert.AlertType.ERROR);
        }
    }

    private void validateFormRemove() {
        if (!checkNotNull()) {
            throw new FieldRequiredNullException();
        }
        if (!invalidCaracter()) {
            throw new CaracterInvalidException();
        }
    }

    private boolean invalidCaracter() {
        try {
            Long.parseLong(txtProductId.getText());
            Integer.parseInt(txtQuantity.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkNotNull() {
        return !(txtProductId.getText() == null || txtProductId.getText().isEmpty() ||
                txtQuantity.getText() == null || txtQuantity.getText().isEmpty());
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void setCartItemService(CartItemService service) {
        this.service = service;
    }
}