module org.example.stockmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.stockmanager to javafx.fxml;
    exports org.example.stockmanager;
}