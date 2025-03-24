package com.accounting.system.controller;

import com.accounting.system.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private CheckBox rememberMeCheckBox;
    
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        // Clear error message on initialization
        errorLabel.setText("");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty");
            return;
        }

        // Validate user (using hardcoded credentials for now, should be replaced with database validation)
        if (validateUser(username, password)) {
            try {
                // Load main interface
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                Parent root = loader.load();
                
                // Create new scene
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
                
                // Get current window and set new scene
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Accounting System - Main");
                stage.show();
            } catch (IOException e) {
                errorLabel.setText("Failed to load main interface: " + e.getMessage());
            }
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }

    @FXML
    private void handleCancel() {
        // Close login window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private boolean validateUser(String username, String password) {
        // Using hardcoded credentials for now, should be replaced with database validation
        String validUsername = "admin";
        String validPasswordHash = hashPassword("admin123");

        return username.equals(validUsername) && 
               hashPassword(password).equals(validPasswordHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
} 