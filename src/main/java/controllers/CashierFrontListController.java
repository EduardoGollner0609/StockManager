package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.entities.Product;

public class CashierFrontListController {

    @FXML
    private TableView<Product> tableViewCart;

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
}
