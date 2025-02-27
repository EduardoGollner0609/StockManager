package org.example.stockmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        double width = screenWidth * 0.8;
        double height = screenHeight * 0.8;

        stage.setWidth(width);
        stage.setHeight(height);

        stage.setTitle("StockManager");

        stage.setScene(scene);
        
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}