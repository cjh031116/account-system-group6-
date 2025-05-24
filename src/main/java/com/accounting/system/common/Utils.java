package com.accounting.system.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

/**
 * Utility class providing common helper methods for the accounting system.
 * Includes methods for date formatting, password hashing, amount handling, and UI dialogs.
 */
public class Utils {
    
    /**
     * Formats a LocalDate object into a string using the system's date format.
     * @param date The date to format
     * @return Formatted date string, or empty string if date is null
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }
    
    /**
     * Parses a date string into a LocalDate object using the system's date format.
     * @param dateStr The date string to parse
     * @return Parsed LocalDate object, or null if string is empty or invalid
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }
    
    /**
     * Hashes a password using SHA-256 algorithm and Base64 encoding.
     * @param password The password to hash
     * @return Hashed password string, or empty string if hashing fails
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Formats a numeric amount to a string with 2 decimal places.
     * @param amount The amount to format
     * @return Formatted amount string
     */
    public static String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }
    
    /**
     * Parses a string into a double amount, removing any non-numeric characters.
     * @param amountStr The amount string to parse
     * @return Parsed amount, or 0.0 if parsing fails
     */
    public static double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr.replaceAll("[^\\d.-]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Shows an error dialog with the specified title and message.
     * @param title The dialog title
     * @param message The error message
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        centerAlert(alert);
        alert.showAndWait();
    }
    
    /**
     * Shows an information dialog with the specified title and message.
     * @param title The dialog title
     * @param message The information message
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        centerAlert(alert);
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog with the specified title and message.
     * @param title The dialog title
     * @param message The confirmation message
     * @return true if user clicks OK, false otherwise
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        centerAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Validates if a string represents a valid non-negative amount.
     * @param amount The amount string to validate
     * @return true if the string is a valid non-negative amount, false otherwise
     */
    public static boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates if a string represents a valid date in the system's format.
     * @param date The date string to validate
     * @return true if the string is a valid date, false otherwise
     */
    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Centers an alert dialog on the screen.
     * @param alert The alert dialog to center
     */
    private static void centerAlert(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.centerOnScreen();
    }
} 