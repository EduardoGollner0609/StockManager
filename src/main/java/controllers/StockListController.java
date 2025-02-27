package controllers;

import db.DB;
import db.DbIntegrityException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listeners.DataChangeListener;
import models.dao.CartItemDao;
import models.dao.DaoFactory;
import models.entities.Product;
import services.ProductService;
import utils.Alerts;
import utils.Utils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockListController implements Initializable, DataChangeListener {

    public static StockListController instance;

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
    private TextField txtSearchProduct;

    @FXML
    private Button btnOpenForm;

    @FXML
    private TableColumn<Product, Product> tableColumnEDIT;

    @FXML
    private TableColumn<Product, Product> tableColumnREMOVE;



    @FXML
    private Button btnReloadTable;

    private ObservableList<Product> productList;

    private ProductService service;


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

        setProductService(new ProductService(DB.getConnection()));
        updateTableView();

        setupSearchListener();

    }

    @FXML
    public void onBtnOpenForm(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Product product = new Product();
        createDialogForm(null, "/org/example/stockmanager/gui/stock-form.fxml", parentStage);
    }

    @FXML
    public void onBtnReloadTable() {
        updateTableView();
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalArgumentException("Service é nulo");
        }

        List<Product> list = service.findAll();
        productList = FXCollections.observableArrayList(list);
        tableViewStock.setItems(productList);

        if (tableColumnEDIT != null) {
            initEditButtons();
        }

        if (tableColumnREMOVE != null) {
            initRemoveButtons();
        }


    }

    public void setProductService(ProductService service) {
        this.service = service;
    }


    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Product, Product>() {

            private final Button button = new Button("edit");

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
                button.setOnAction(
                        event -> createDialogForm(obj, "/org/example/stockmanager/gui/stock-form.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void createDialogForm(Product obj, String absoluteName, Stage parentStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            StockFormController controller = loader.getController();
            controller.subscribeDataChangeListener(this);
            CashierFrontListController cashierFrontController = loader.getController();
            Stage dialogStage = new Stage();

            if (obj != null) {
                controller.setProduct(obj);
                controller.setProductService(new ProductService());
                controller.updateFormData();
                dialogStage.setTitle("StockManager - Atualizar Produto");
            } else {
                dialogStage.setTitle("StockManager - Criar Produto");
            }


            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("Erro na hora de carregar tela.", "Erro ao carregar tela. ", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initRemoveButtons() {

        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Product, Product>() {

            private final Button button = new Button("remove");

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
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }


    private void removeEntity(Product obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Deletar produto", "Tem certeza que desejo remover o produto " + obj.getName() + "?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.deleteById(obj.getId());
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Erro ao remover produto", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

}