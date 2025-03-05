package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn<SaleItem, String> tableColumnSaleDate;

    @FXML
    private TableColumn<SaleItem, String> tableColumnObservation;


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
                new SimpleStringProperty(cellData.getValue().getObservation()));

        if (saleItemService == null) {
            setSaleItemService(new SaleItemService());
        }

        updateTableView();

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
