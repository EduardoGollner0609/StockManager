package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Alerts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ApplicationController implements Initializable {

    @FXML
    private Button btnCreateProductView;

    @FXML
    public void onbtnCreateProduct() {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}