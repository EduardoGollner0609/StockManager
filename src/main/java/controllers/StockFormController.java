package controllers;

import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.entities.Product;
import services.ProductService;
import utils.Alerts;

import exceptions.CaracterInvalidException;
import exceptions.FieldRequiredNullException;
import utils.Utils;

public class StockFormController {


    private static final String ERROR_FORM_TITLE = "Erro no formulário.";

    private Product product;

    private ProductService productService;

    private Long productId;

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
    public void onBtnCreateConfirm(ActionEvent event) {

        String nameTxt = txtName.getText();
        String quantityTxt = txtQuantity.getText();
        String priceTxt = replaceComma(txtPrice.getText());
        String descriptionTxt = txtDescription.getText();


        product = validateAndCreateDataProduct(nameTxt, quantityTxt, priceTxt, descriptionTxt);

        if (product != null) {
            try {
                productService.saveOrUpdate(product);

                Alerts.showAlert("Sucesso", null, "Salvo com sucesso", Alert.AlertType.INFORMATION);

                StockListController.instance.updateTableView();

                Utils.currentStage(event).close();

                if (product.getId() != null) {
                    CashierFrontListController.instance.updateTableView();
                }
            } catch (DbException e) {
                Alerts.showAlert(ERROR_FORM_TITLE, null, "Erro ao salvar o usuário", Alert.AlertType.ERROR);
            }
        }
    }

    private Product validateAndCreateDataProduct(String nameTxt, String quantityTxt, String priceTxt, String descriptionTxt) {
        try {
            validateForm(nameTxt, quantityTxt, priceTxt, descriptionTxt);
            if (productId != null) {
                return new Product(
                        productId,
                        nameTxt,
                        descriptionTxt,
                        Integer.parseInt(quantityTxt),
                        Double.parseDouble(priceTxt)
                );
            }
            return new Product(
                    nameTxt,
                    descriptionTxt,
                    Integer.parseInt(quantityTxt),
                    Double.parseDouble(priceTxt)
            );

        } catch (FieldRequiredNullException e) {
            Alerts.showAlert(ERROR_FORM_TITLE, null, "Todos os campos devem estar preenchidos.", Alert.AlertType.ERROR);
            return null;
        } catch (CaracterInvalidException e) {
            Alerts.showAlert(ERROR_FORM_TITLE, null, "Os Campos de quantidade e preço não podem possuir letras, caracteres inválidos ou números menores que zeros. No campo de preço colocar virgula/ponto somente nas casas decimais, exemplo: 1999,99.", Alert.AlertType.ERROR);
            return null;
        }
    }

    private void validateForm(String nameTxt, String quantityTxt, String priceTxt, String descriptionTxt) {
        if (!checkNotNull(nameTxt, quantityTxt, priceTxt, descriptionTxt)) {
            throw new FieldRequiredNullException();
        }
        if (!checkInvalidCaracter(quantityTxt, priceTxt)) {
            throw new CaracterInvalidException();
        }
    }

    private boolean checkNotNull(String nameTxt, String quantityTxt, String priceTxt, String descriptionTxt) {
        return !(nameTxt == null || nameTxt.trim().isEmpty() ||
                quantityTxt == null || quantityTxt.trim().isEmpty() ||
                priceTxt == null || priceTxt.trim().isEmpty() ||
                descriptionTxt == null || descriptionTxt.trim().isEmpty());
    }

    private boolean checkInvalidCaracter(String quantityTxt, String priceTxt) {
        try {
            if (Integer.parseInt(quantityTxt) < 0) {
                return false;
            }
            if (Double.parseDouble(priceTxt) < 0) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void updateFormData() {

        if (product == null) {
            throw new IllegalStateException("Produto é nulo");
        }

        productId = product.getId();
        txtName.setText(product.getName());
        txtDescription.setText(product.getDescription());
        txtQuantity.setText(String.valueOf(product.getQuantity()));
        txtPrice.setText(String.valueOf(product.getPrice()));

    }

    private String replaceComma(String priceTxt) {
        return priceTxt.trim().replace(",", ".");
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

}
