module org.example.stockmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.compiler;
    requires java.desktop;


    opens org.example.stockmanager to javafx.fxml;
    exports org.example.stockmanager;
    exports controllers;
    opens controllers to javafx.fxml;

    opens models.entities to javafx.base;
}