package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import models.entities.CartItem;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmPaymentController implements Initializable {

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
    private Text totalValue;

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

    }

    @FXML
    public void onBtnCancelPayment() {
        System.out.print("Cancelar compra");
    }

    @FXML
    public void onBtnConfirmPayment() {
        System.out.print("Confirmar compra");
    }
}
