package controllers;

import db.DB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import listeners.DataChangeListener;
import models.entities.CartItem;
import models.entities.Product;
import org.example.stockmanager.Application;
import services.CartItemService;
import services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CashierFrontListController implements Initializable, DataChangeListener {

    private CartItemService service;

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

    public void setCartItemService(CartItemService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {

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

        setCartItemService(new CartItemService(DB.getConnection()));
        updateTableView();
    }

    private void updateTableView() {

        if (service == null) {
            throw new IllegalArgumentException("Service é nulo");
        }

        List<CartItem> list = service.findAll();
        cartItemList = FXCollections.observableArrayList(list);
        tableViewCart.setItems(cartItemList);
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
