package com.accounting.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountingSystemApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load login interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        
        // Create scene with specific size
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        
        // Center the window on screen
        primaryStage.centerOnScreen();
        
        // Set window properties
        primaryStage.setTitle("Accounting System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
