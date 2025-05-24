package com.accounting.system.controller;

import com.accounting.system.common.Constants;
import com.accounting.system.model.User;
import com.accounting.system.service.UserService;
import com.accounting.system.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Controller class for the login screen.
 * Handles user authentication and navigation to the main application.
 */
public class LoginController {
    // UI Components
    @FXML
    private TextField usernameField; // Text field for entering username
    @FXML
    private PasswordField passwordField; // Password field for entering password
    @FXML
    private CheckBox rememberMeCheckbox; // Checkbox for remembering user credentials
    @FXML
    private Button loginButton; // Button to initiate login process
    @FXML
    private Label errorLabel;

    // Service dependencies
    private final UserService userService; // Service for user-related operations

    /**
     * Default constructor initializes the controller with required services.
     */
    public LoginController() {
        this.userService = new UserServiceImpl();
    }

    /**
     * Initializes the controller after FXML loading.
     * Sets up event handlers and initial state.
     */
    @FXML
    public void initialize() {
        loadSavedCredentials();
        setupEventHandlers();

        // Clear error label when input changes
        usernameField.textProperty().addListener((obs, old, newVal) ->
                errorLabel.setText(""));
        passwordField.textProperty().addListener((obs, old, newVal) ->
                errorLabel.setText(""));
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> handleLogin());
    }

    /**
     * Loads saved credentials if they exist.
     */
    private void loadSavedCredentials() {
        if (rememberMeCheckbox == null) {
            return; // Skip if checkbox is not initialized
        }

        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        String savedUsername = prefs.get("username", "");
        String savedPassword = prefs.get("password", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            usernameField.setText(savedUsername);
            passwordField.setText(savedPassword);
            rememberMeCheckbox.setSelected(true);
        }
    }

    /**
     * Handles the login process when the login button is clicked.
     * Validates credentials and navigates to the main screen if successful.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() && password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        } else if (username.isEmpty()) {
            showError("Please enter your username");
            return;
        } else if (password.isEmpty()) {
            showError("Please enter your password");
            return;
        }

        try {
            User user = userService.authenticate(username, password);
            if (user == null) {
                showError("Invalid username or password");
                return;
            }

            if (!user.isActive()) {
                showError("Your account is not active. Please contact the administrator.");
                return;
            }

            // 检查rememberMeCheckbox是否为null
            if (rememberMeCheckbox != null) {
                if (rememberMeCheckbox.isSelected()) {
                    saveCredentials(username, password);
                } else {
                    clearSavedCredentials();
                }
            }

            navigateToMainScreen(user);
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
        }
    }

    /**
     * Saves user credentials to preferences.
     *
     * @param username The username to save
     * @param password The password to save
     */
    private void saveCredentials(String username, String password) {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        prefs.put("username", username);
        prefs.put("password", password);
    }

    /**
     * Clears saved credentials from preferences.
     */
    private void clearSavedCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        prefs.remove("username");
        prefs.remove("password");
    }

    /**
     * Navigates to the main application screen.
     * Loads the main FXML file and sets up the new scene.
     */
    private void navigateToMainScreen(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.initData(user);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(Constants.APP_TITLE);

            // 设置窗口大小
            stage.setWidth(1380);
            stage.setHeight(768);

            // 居中显示
            stage.centerOnScreen();

            // 设置最小窗口大小
            stage.setMinWidth(1024);
            stage.setMinHeight(768);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load main screen");
        }
    }

    /**
     * Shows an error message in the error label.
     *
     * @param message The error message to display
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Handles the cancel button click.
     * Closes the login window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}