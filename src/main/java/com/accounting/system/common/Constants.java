package com.accounting.system.common;

/**
 * Class containing constant values used throughout the application.
 * Includes application settings, UI configurations, and message constants.
 */
public class Constants {
    // Application information
    public static final String APP_TITLE = "Accounting System"; // Application title1
    public static final String APP_LOGIN_TITLE = "Accounting System - Login"; // Login window title
    public static final String APP_VERSION = "1.0.0"; // Application version 1.0.0
    
    // Window dimensions
    public static final double LOGIN_WINDOW_WIDTH = 500; // Login window width in pixels
    public static final double LOGIN_WINDOW_HEIGHT = 400; // Login window height in pixels
    public static final double MAIN_WINDOW_WIDTH = 1024; // Main window width in pixels
    public static final double MAIN_WINDOW_HEIGHT = 768; // Main window height in pixels
    
    // Resource paths
    public static final String FXML_PATH = "/fxml/"; // Path to FXML files
    public static final String STYLES_PATH = "/styles/"; // Path to CSS files
    public static final String LOGIN_FXML = FXML_PATH + "login.fxml"; // Login screen FXML file path
    public static final String MAIN_FXML = FXML_PATH + "main.fxml"; // Main screen FXML file path
    public static final String VOUCHER_ENTRY_FXML = FXML_PATH + "voucher_entry.fxml"; // Voucher entry screen FXML file path
    
    // CSS style classes
    public static final String BUTTON_PRIMARY = "button-primary"; // Primary button style class
    public static final String BUTTON_SECONDARY = "button-secondary"; // Secondary button style class
    public static final String BUTTON_DANGER = "button-danger"; // Danger button style class

    // Date and time formats
    public static final String DATE_FORMAT = "yyyy-MM-dd"; // Standard date format pattern
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // Standard date-time format pattern
    
    // Error messages
    public static final String ERROR_REQUIRED_FIELD = "This field is required"; // Required field validation error
    public static final String ERROR_INVALID_DATE = "Invalid date format"; // Invalid date validation error
    public static final String ERROR_INVALID_AMOUNT = "Invalid amount"; // Invalid amount validation error
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password"; // Authentication error
    
    // Success messages
    public static final String SUCCESS_SAVE = "Successfully saved"; // Save operation success message
    public static final String SUCCESS_DELETE = "Successfully deleted"; // Delete operation success message
    public static final String SUCCESS_UPDATE = "Successfully updated"; // Update operation success message
} 