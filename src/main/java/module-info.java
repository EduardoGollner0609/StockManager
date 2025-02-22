module org.example.stockmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.stockmanager to javafx.fxml;
    exports org.example.stockmanager;
}