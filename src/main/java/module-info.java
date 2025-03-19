module com.accounting.system.accountingsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.accounting.system to javafx.fxml;
    exports com.accounting.system;
}