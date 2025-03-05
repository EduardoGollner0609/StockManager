package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import models.entities.CartItem;
import models.entities.Client;
import models.entities.Sale;
import models.entities.SaleItem;
import services.*;
import utils.Alerts;
import utils.Utils;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmPaymentController implements Initializable {

    private ProductService productService;

    private ObservableList<CartItem> cartItemList;

    private SaleService saleService;

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

        if (saleService == null) {
            setSaleService(new SaleService(new ClientService(), new SaleItemService(), new CartItemService(new ProductService())));
        }

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
    public void onBtnConfirmPayment(ActionEvent event) {

        String paymentMethod = getPaymentMethod();

        Client client = instantiateClient();

        if (client != null && paymentMethod != null) {
            Sale sale = instantiateSale(client, paymentMethod);
            fillingSaleItems(sale);
            saleService.insert(sale);

            CashierFrontListController.instance.updateTableView();

            HistorySalesController.instance.updateTableView();

            Utils.currentStage(event).close();
        }
    }

    private Client instantiateClient() {
        boolean clientFormIsValid = !(txtClientName.getText() == null || txtClientName.getText().isEmpty() || txtClientPhone == null || txtClientPhone.getText().isEmpty());

        if (clientFormIsValid) {
            return new Client(txtClientName.getText(), txtClientPhone.getText());
        }

        Alerts.showAlert(
                "Erro ao finalizar compra",
                null, "Favor informar o nome, número do cliente e a forma de pagamento.",
                Alert.AlertType.ERROR);
        return null;

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

    public void setSaleService(SaleService saleService) {
        this.saleService = saleService;
    }

    private void fillingSaleItems(Sale sale) {
        for (CartItem cartItem : cartItemList) {
            SaleItem item = new SaleItem(cartItem);
            item.setSale(sale);
            sale.addItem(item);
        }
    }

    private String getPaymentMethod() {
        List<String> options = new ArrayList<>();

        if (pixOption.isSelected()) {
            options.add(pixOption.getText());
        }
        if (cardOption.isSelected()) {
            options.add(cardOption.getText());
        }
        if (moneyOption.isSelected()) {
            options.add(moneyOption.getText());
        }

        if (options.size() != 1) {
            return null;
        }

        return options.getFirst();
    }

    private Sale instantiateSale(Client client, String paymentMethod) {
        return new Sale(
                client,
                LocalDateTime.now(),
                calculateTotalValue(),
                paymentMethod,
                txtObservation.getText()
        );
    }

}
