package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.entities.SaleItem;
import services.SaleItemService;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HistorySalesController implements Initializable {

    public static HistorySalesController instance;

    private SaleItemService saleItemService;

    private ObservableList<SaleItem> saleItemsList;

    @FXML
    private TableView<SaleItem> tableViewSalesItems;

    @FXML
    private TableColumn<SaleItem, String> tableColumnProductName;

    @FXML
    private TableColumn<SaleItem, Integer> tableColumnQuantity;

    @FXML
    private TableColumn<SaleItem, String> tableColumnPrice;

    @FXML
    private TableColumn<SaleItem, String> tableColumnTotalValue;

    @FXML
    private TableColumn<SaleItem, String> tableColumnClientName;

    @FXML
    private TableColumn<SaleItem, String> tableColumnClientPhone;

    @FXML
    private TableColumn<SaleItem, String> tableColumnClientCpf;

    @FXML
    private TableColumn<SaleItem, String> tableColumnSaleDate;

    @FXML
    private TableColumn<SaleItem, String> tableColumnObservation;

    @FXML
    private TextField txtSearchProductName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {

        instance = this;

        tableColumnProductName.setCellValueFactory(cellData ->
                new SimpleStringProperty((cellData.getValue().getProduct().getName())));

        tableColumnQuantity.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        tableColumnClientCpf.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSale().getClient().getCpf()));

        tableColumnPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getPrice())));

        tableColumnTotalValue.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getTotalValue())));

        tableColumnClientName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSale().getClient().getName()));

        tableColumnClientPhone.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSale().getClient().getPhone()));

        tableColumnSaleDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSale().getSaleDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        tableColumnObservation.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSale().getObservation()));

        if (saleItemService == null) {
            setSaleItemService(new SaleItemService());
        }

        updateTableView();

        setupSearchListener();


    }

    private void setupSearchListener() {
        txtSearchProductName.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProductList(newValue);
        });
    }

    private void filterProductList(String searchQuery) {
        ObservableList<SaleItem> filteredList = FXCollections.observableArrayList();
        for (SaleItem saleItem : saleItemsList) {
            if (saleItem.getProduct().getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                filteredList.add(saleItem);
            }
        }
        tableViewSalesItems.setItems(filteredList);
    }

    public void updateTableView() {
        if (saleItemService == null) {
            throw new IllegalArgumentException("Service é nulo");
        }


        List<SaleItem> items = saleItemService.findAll();

        saleItemsList = FXCollections.observableArrayList(items);
        tableViewSalesItems.setItems(saleItemsList);
        tableViewSalesItems.refresh();
    }

    public void setSaleItemService(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }
}
