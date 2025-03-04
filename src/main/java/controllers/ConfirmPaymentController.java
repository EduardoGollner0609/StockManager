package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import models.entities.CartItem;
import services.ProductService;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmPaymentController implements Initializable {

    private ProductService productService;

    private ObservableList<CartItem> cartItemList;

    @FXML
    private TextField txtClientName;

    @FXML
    private TextField txtClientPhone;

    @FXML
    private TextArea txtObservation;

    @FXML
    private RadioButton moneyOption;

    @FXML
    private RadioButton cardOption;

    @FXML
    private RadioButton pixOption;

    @FXML
    private Text txtTotalValue;

    @FXML
    private Button btnCancelPayment;

    @FXML
    private Button btnConfirmPayment;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {

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

    }

    public void loadTableCartList() {
        tableViewCart.setItems(cartItemList);
        txtTotalValue.setText(String.format("R$ %.2f", calculateTotalValue()));
    }

    @FXML
    public void onBtnCancelPayment() {
        System.out.print("Cancelar compra");
    }

    @FXML
    public void onBtnConfirmPayment() {
        System.out.print("Confirmar compra");
    }

    public void setCartItemList(ObservableList<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    private Double calculateTotalValue() {
        Double totalValue = 0.00;
        for (CartItem cartItem : cartItemList) {
            totalValue += cartItem.getTotalValue();
        }
        return totalValue;
    }
}
