module com.accounting.system.accountingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.accounting.system to javafx.fxml;
    opens com.accounting.system.controller to javafx.fxml;
    exports com.accounting.system;
    exports com.accounting.system.controller;
    exports com.accounting.system.model;
}