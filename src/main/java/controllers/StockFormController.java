package controllers;

import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import listeners.DataChangeListener;
import models.dao.DaoFactory;
import models.dao.ProductDao;
import models.entities.Product;
import services.ProductService;
import utils.Alerts;

import exceptions.CaracterInvalidException;
import exceptions.FieldRequiredNullException;
import utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StockFormController implements Initializable {

    private final String errorFormTitle = "Erro no formulário.";

    private Product product;

    private ProductService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
                ProductDao productDao = DaoFactory.createProduct();

                productDao.saveOrUpdate(product);

                Alerts.showAlert("Sucesso", null, "Salvo com sucesso", Alert.AlertType.INFORMATION);
                notifyDataChangeListeners();
                Utils.currentStage(event).close();
            } catch (DbException e) {
                Alerts.showAlert(errorFormTitle, null, "Erro ao salvar o usuário", Alert.AlertType.ERROR);
            }
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductService(ProductService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }


    private void validateForm(String nameTxt, String quantityTxt, String priceTxt, String descriptionTxt) {
        if (!checkNotNull(nameTxt, quantityTxt, priceTxt, descriptionTxt)) {
            throw new FieldRequiredNullException();
        }
        if (!checkInvalidCaracter(quantityTxt, priceTxt)) {
            throw new CaracterInvalidException();
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
            Alerts.showAlert(errorFormTitle, null, "Todos os campos devem estar preenchidos.", Alert.AlertType.ERROR);
            return null;
        } catch (CaracterInvalidException e) {
            Alerts.showAlert(errorFormTitle, null, "Campo de quantidade e preço não podem possuir letras ou caracteres inválidos.", Alert.AlertType.ERROR);
            return null;
        }

    }

    private String replaceComma(String priceTxt) {
        return priceTxt.trim().replace(",", ".");
    }

    private boolean checkNotNull(String nameTxt, String quantityTxt, String priceTxt, String descriptionTxt) {
        return !(nameTxt == null || nameTxt.trim().isEmpty() ||
                quantityTxt == null || quantityTxt.trim().isEmpty() ||
                priceTxt == null || priceTxt.trim().isEmpty() ||
                descriptionTxt == null || descriptionTxt.trim().isEmpty());
    }

    private boolean checkInvalidCaracter(String quantityTxt, String priceTxt) {
        try {
            Integer.parseInt(quantityTxt);
            Double.parseDouble(priceTxt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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


}
