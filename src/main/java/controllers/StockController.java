package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.dao.DaoFactory;
import models.entities.Product;

public class StockController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnCreateConfirm;

    @FXML
    public void btnCreateConfirmClick() {
        Product product = new Product(null, txtName.getText(), txtDescription.getText(), Integer.parseInt(txtQuantity.getText()), Double.parseDouble(txtPrice.getText()));
        DaoFactory.createProduct().insert(product);
    }
}
