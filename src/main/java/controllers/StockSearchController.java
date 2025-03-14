package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import models.entities.Product;
import services.CartItemService;
import services.ProductService;
import utils.Alerts;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StockSearchController implements Initializable {

    public static StockSearchController instance;

    private ProductService productService;

    @FXML
    private TableView<Product> tableViewStock;

    @FXML
    private TableColumn<Product, Integer> tableColumnId;

    @FXML
    private TableColumn<Product, String> tableColumnName;

    @FXML
    private TableColumn<Product, String> tableColumnDescription;

    @FXML
    private TableColumn<Product, Integer> tableColumnQuantity;

    @FXML
    private TableColumn<Product, String> tableColumnPrice;

    @FXML
    private TableColumn<Product, Product> tableColumnADDCART;

    @FXML
    private TextField txtSearchProduct;

    private ObservableList<Product> productList;


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

        if (productService == null) {
            setProductService(new ProductService());
        }
        updateTableView();

        setupSearchListener();
    }

    public void updateTableView() {
        if (productService == null) {
            throw new IllegalArgumentException("Service é nulo");
        }

        List<Product> list = productService.findAll();
        productList = FXCollections.observableArrayList(list);
        tableViewStock.setItems(productList);

        if (tableColumnADDCART != null) {
            initAddCartButtons();
        }
    }

    private void setupSearchListener() {
        txtSearchProduct.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProductList(newValue);
        });
    }

    private void filterProductList(String searchQuery) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                filteredList.add(product);
            }
        }
        tableViewStock.setItems(filteredList);
    }

    private void initAddCartButtons() {

        tableColumnADDCART.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnADDCART.setCellFactory(param -> new TableCell<Product, Product>() {

            private final Button button = new Button("Adicionar");

            {
                button.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(Product obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createConfirmQuantityView(obj, Utils.currentStage(event)));
            }
        });
    }

    private void createConfirmQuantityView(Product obj, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/stockmanager/gui/confirm-quantity.fxml"));
            Pane pane = loader.load();

            ConfirmQuantityController controller = loader.getController();
            controller.setProductAndTxtNameProduct(obj);
            controller.setCartItemService(new CartItemService(productService));

            Stage stage = new Stage();

            stage.setScene(new Scene(pane));
            stage.setTitle("StockManager");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.getMessage();
            Alerts.showAlert("Erro na hora de carregar tela.", "Erro ao carregar tela. ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}