package com.accounting.system.common;

public class Constants {
    // Application constants
    public static final String APP_TITLE = "Accounting System";
    public static final String APP_VERSION = "1.0.0";
    
    // Window sizes
    public static final double LOGIN_WINDOW_WIDTH = 500;
    public static final double LOGIN_WINDOW_HEIGHT = 400;
    public static final double MAIN_WINDOW_WIDTH = 1024;
    public static final double MAIN_WINDOW_HEIGHT = 768;
    
    // File paths
    public static final String FXML_PATH = "/fxml/";
    public static final String STYLES_PATH = "/styles/";
    public static final String LOGIN_FXML = FXML_PATH + "login.fxml";
    public static final String MAIN_FXML = FXML_PATH + "main.fxml";
    public static final String VOUCHER_ENTRY_FXML = FXML_PATH + "voucher_entry.fxml";
    
    // CSS classes
    public static final String BUTTON_PRIMARY = "button-primary";
    public static final String BUTTON_SECONDARY = "button-secondary";
    public static final String BUTTON_DANGER = "button-danger";
    
    // Database
    public static final String DB_URL = "jdbc:mysql://localhost:3306/accounting_system";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";
    
    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // Validation messages
    public static final String ERROR_REQUIRED_FIELD = "This field is required";
    public static final String ERROR_INVALID_DATE = "Invalid date format";
    public static final String ERROR_INVALID_AMOUNT = "Invalid amount";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    
    // Success messages
    public static final String SUCCESS_SAVE = "Successfully saved";
    public static final String SUCCESS_DELETE = "Successfully deleted";
    public static final String SUCCESS_UPDATE = "Successfully updated";
} 