module com.example.manager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.manager to javafx.fxml;
    exports com.example.manager;
}