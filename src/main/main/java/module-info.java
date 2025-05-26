/**
 * Module definition for the Accounting System application.
 * Declares module dependencies and exports.
 */
module com.accounting.system {
    // Required JavaFX modules
    requires javafx.controls; // For JavaFX UI controls
    requires javafx.fxml; // For FXML support

    // Required Java modules
    requires java.sql; // For database operations
    requires java.base; // For basic Java functionality
    requires java.prefs;

    // Required third-party modules
    requires com.fasterxml.jackson.databind; // For JSON processing
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.desktop;
    requires okhttp;
    requires fastjson;
    requires com.alibaba.fastjson2; // For Java 8 date/time support

    // Export packages for other modules to use
    exports com.accounting.system; // Main package
    exports com.accounting.system.controller; // UI controllers
    exports com.accounting.system.model; // Data models
    exports com.accounting.system.service; // Service interfaces
    exports com.accounting.system.service.impl; // Service implementations
    exports com.accounting.system.common; // Common utilities
    exports com.accounting.system.component; // Custom UI components

    // Open packages for FXML to access
    opens com.accounting.system.controller to javafx.fxml; // Allow FXML to access controllers
    opens com.accounting.system.model to javafx.base;
}