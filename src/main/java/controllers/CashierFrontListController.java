package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.entities.Product;
import org.example.stockmanager.Application;

import java.io.IOException;

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

    @FXML
    private Button btnOpenStockSearch;

    @FXML
    public void onBtnOpenStockSearch() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/stockmanager/gui/stock-search.fxml"));
        Pane pane = loader.load();

        Stage stage = new Stage();

        stage.setScene(new Scene(pane));
        stage.setTitle("StockManager");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.show();
    }

}
