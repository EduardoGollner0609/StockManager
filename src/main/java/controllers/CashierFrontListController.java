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
import models.entities.CartItem;
import services.CartItemService;
import services.ProductService;
import utils.Alerts;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CashierFrontListController implements Initializable, DataChangeListener {


    public static CashierFrontListController instance;

    private CartItemService service;

    private ObservableList<CartItem> cartItemList;

    @FXML
    private TextField txtProductId;

    @FXML
    private TextField txtQuantity;

    @FXML
    private Button btnRemove;

    @FXML
    private TableView<CartItem> tableViewCart;

    @FXML
    private TableColumn<CartItem, Long> tableColumnId;

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

    @FXML
    private Button btnOpenStockSearch;

    @FXML
    private Button btnReloadTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {

        instance = this;

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableColumnPrice.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("R$ %.2f", data.getValue().getPrice()
                )));
        tableColumnTotalValue.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("R$ %.2f", data.getValue().getTotalValue()
                )));

        setCartItemService(new CartItemService(new ProductService()));

        updateTableView();
    }

    public void updateTableView() {

        if (service == null) {
            throw new IllegalArgumentException("Service é nulo");
        }

        List<CartItem> list = service.findAll();


        cartItemList = FXCollections.observableArrayList(list);

        tableViewCart.setItems(cartItemList);
        tableViewCart.refresh();
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


            StockListController.instance.updateTableView();

            updateTableView();

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
                    "Os Campos de quantidade e código do produto não podem possuir letras, caracteres inválidos, ou números abaixo de zero.",
                    Alert.AlertType.ERROR);
        } catch (ResourceNotFoundException e) {
            Alerts.showAlert(
                    "Erro ao remover quantidade",
                    null,
                    "Não foi encontrado o produto.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtnReloadTable() {
        updateTableView();
    }

    private void validateFormRemove() {
        if (!checkNotNull()) {
            throw new FieldRequiredNullException();
        }
        if (!isValidCaracter()) {
            throw new CaracterInvalidException();
        }
    }

    private boolean isValidCaracter() {
        try {
            if (Integer.parseInt(txtQuantity.getText()) < 0) {
                return false;
            }
            Long.parseLong(txtProductId.getText());
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