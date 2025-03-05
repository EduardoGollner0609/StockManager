package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.entities.SaleItem;
import services.SaleItemService;

import java.net.URL;
import java.util.ResourceBundle;

public class HistorySalesController implements Initializable {

    private SaleItemService saleItemService;

    private ObservableList<SaleItem> saleItemsList;

    @FXML
    private TableView<SaleItem> tableViewSales;

    @FXML
    private TableView<SaleItem> tableViewCart;

    @FXML
    private TableColumn<SaleItem, Long> tableColumnId;

    @FXML
    private TableColumn<SaleItem, String> tableColumnName;

    @FXML
    private TableColumn<SaleItem, String> tableColumnDescription;

    @FXML
    private TableColumn<SaleItem, Integer> tableColumnQuantity;

    @FXML
    private TableColumn<SaleItem, String> tableColumnPrice;

    @FXML
    private TableColumn<SaleItem, String> tableColumnTotalValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {

        if (saleItemService == null) {
            setSaleItemService(new SaleItemService());
        }


    }

    public void setSaleItemService(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }
}
