package com.accounting.system.controller;

import com.accounting.system.common.Constants;
import com.accounting.system.common.Utils;
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

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private CheckBox rememberMeCheckbox;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorLabel;

    private final UserService userService;

    public LoginController() {
        this.userService = new UserServiceImpl();
    }

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        setupValidation();
        setupRememberMe();
        
        // Clear error label when input changes
        usernameField.textProperty().addListener((obs, old, newVal) -> 
            errorLabel.setText(""));
        passwordField.textProperty().addListener((obs, old, newVal) -> 
            errorLabel.setText(""));
    }

    private void setupValidation() {
        loginButton.disableProperty().bind(
            usernameField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
        );
    }

    private void setupRememberMe() {
        String savedUsername = userService.getSavedUsername();
        if (savedUsername != null && !savedUsername.isEmpty()) {
            usernameField.setText(savedUsername);
            rememberMeCheckbox.setSelected(true);
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        try {
            if (userService.authenticate(username, password)) {
                if (rememberMeCheckbox.isSelected()) {
                    userService.saveUsername(username);
                } else {
                    userService.clearSavedUsername();
                }
                
                // Load main window
                loadMainWindow(new User(username, password));
                
                // Close login window
                ((Stage) loginButton.getScene().getWindow()).close();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            errorLabel.setText("Login failed: " + e.getMessage());
        }
    }

    private void loadMainWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            
            MainController mainController = loader.getController();
            mainController.initData(user);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle(Constants.APP_TITLE + " - Main");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading main.fxml: " + e.getMessage());
            e.printStackTrace();
            Utils.showError("Error", "Failed to load main interface: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
} 