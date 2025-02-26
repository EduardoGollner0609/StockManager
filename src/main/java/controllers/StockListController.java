package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.entities.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StockListController implements Initializable {

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
    private TableColumn<Product, Double> tableColumnPrice;

    @FXML
    private Button btnCreateNewProduct;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    @FXML
    public void onBtnCreateNewProduct() {
        try {
            Stage stage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("/org/example/stockmanager/gui/stock-create.fxml"));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar a tela.");
        }
    }
}
