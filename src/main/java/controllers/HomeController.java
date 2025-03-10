package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Text txtDay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {
        LocalDate today = LocalDate.now();
        txtDay.setText(String.format("Dia: %d/%d/%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear()));
    }


}
