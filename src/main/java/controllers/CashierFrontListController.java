package controllers;

import exceptions.CaracterInvalidException;
import exceptions.FieldRequiredNullException;
import exceptions.ResourceNotFoundException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.entities.CartItem;
import services.CartItemService;
import services.ProductService;
import utils.Alerts;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CashierFrontListController implements Initializable {

    public static CashierFrontListController instance;

    private CartItemService cartItemService;

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
    private TableColumn<CartItem, CartItem> tableColumnREMOVE;

    @FXML
    private Button btnOpenStockSearch;

    @FXML
    private Button btnReloadTable;

    @FXML
    private Button btnOpenConfirmPayment;

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

        if (cartItemService == null) {
            setCartItemService(new CartItemService(new ProductService()));
        }

        initRemoveButtons();

        updateTableView();
    }

    public void updateTableView() {

        if (cartItemService == null) {
            throw new IllegalArgumentException("Service é nulo");
        }

        List<CartItem> list = cartItemService.findAll();


        cartItemList = FXCollections.observableArrayList(list);

        tableViewCart.setItems(cartItemList);
        tableViewCart.refresh();
    }


    @FXML
    public void onBtnOpenStockSearch(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(null, "Buscar Produtos", "/org/example/stockmanager/gui/stock-search.fxml", parentStage);
    }

    @FXML
    public void onBtnConfirmPayment(ActionEvent event) {
        if (cartItemList.isEmpty()) {
            Alerts.showAlert(
                    "Erro ao finalizar compra",
                    null,
                    "O carrinho está vazio, é necessário que tenha ao menos um item",
                    Alert.AlertType.ERROR);
            return;
        }
        Stage parentStage = Utils.currentStage(event);
        createDialogForm(null, "Confirmar Compra", "/org/example/stockmanager/gui/confirm-payment.fxml", parentStage);
    }

    private void createDialogForm(CartItem obj, String title, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            Stage dialogStage = new Stage();

            if (obj != null) {
                ConfirmQuantityController controller = loader.getController();
                controller.setCartItem(obj);
                controller.setCartItemService(new CartItemService(new ProductService()));
            }

            Object controller = loader.getController();

            if (controller instanceof ConfirmPaymentController) {
                ((ConfirmPaymentController) controller).setCartItemList(cartItemList);
                ((ConfirmPaymentController) controller).loadTableCartList();
            }

            dialogStage.setTitle("Stock Manager - " + title);
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("Erro na hora de carregar tela.", "Erro ao carregar tela. ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void onBtnRemove() {
        try {
            validateFormRemove();

            Integer quantity = Integer.parseInt(txtQuantity.getText());
            Long productId = Long.parseLong(txtProductId.getText());


            cartItemService.removeQuantityFromCart(productId, quantity);


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

    public void setCartItemService(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }


    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<CartItem, CartItem>() {

            private final Button button = new Button("Remover");

            {
                button.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(CartItem obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(obj, "Remover Quantidade", "/org/example/stockmanager/gui/confirm-quantity.fxml", Utils.currentStage(event)));
            }
        });
    }
}