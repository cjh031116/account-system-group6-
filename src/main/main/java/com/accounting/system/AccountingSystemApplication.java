package com.accounting.system;

import com.accounting.system.common.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Accounting System.plication.
 * This class serves as the entry point for the JavaFX ap
 */
public class AccountingSystemApplication extends Application {

    /**
     * The main method that launches the JavaFX application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and starts the JavaFX application.
     * Loads the login screen and sets up the primary stage.
     * @param primaryStage The primary stage for the application
     * @throws Exception If there is an error loading the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen FXML file
        // This file contains the layout and design of the login screen
        // It is located in the resources directory under fxml/login.fxml
        Parent root = FXMLLoader.load(getClass().getResource(Constants.LOGIN_FXML));
        
        // Create a new scene with the loaded FXML content
        Scene scene = new Scene(root, Constants.LOGIN_WINDOW_WIDTH, Constants.LOGIN_WINDOW_HEIGHT);
        
        // Set the application title
        primaryStage.setTitle(Constants.APP_LOGIN_TITLE);
        
        // Set the scene on the primary stage
        primaryStage.setScene(scene);
        
        // Prevent window resizing
        primaryStage.setResizable(false);
        
        // Center the window on the screen
        primaryStage.centerOnScreen();
        
        // Show the primary stage
        primaryStage.show();
    }
}
