package com.accounting.system.controller;

import com.accounting.system.common.Utils;
import com.accounting.system.model.Voucher;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for the voucher list view.
 * Handles displaying and managing a list of vouchers.
 */
public class VoucherListController {
    // Service dependencies
    private final VoucherService voucherService;
    
    // UI Components
    @FXML private TableView<Voucher> voucherTable;
    @FXML private TableColumn<Voucher, String> voucherNoColumn;
    @FXML private TableColumn<Voucher, LocalDate> dateColumn;
    @FXML private TableColumn<Voucher, String> descriptionColumn;
    @FXML private TableColumn<Voucher, Double> debitColumn;
    @FXML private TableColumn<Voucher, Double> creditColumn;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button searchButton;
    @FXML private Button exportButton;
    @FXML private Label statusLabel;

    /**
     * Constructor initializes the controller with required services.
     */
    public VoucherListController() {
        this.voucherService = new VoucherServiceImpl();
    }

    /**
     * Initializes the controller.
     * Sets up table columns and loads initial data.
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        setDefaultDates();
        setupButtonActions();
        loadVouchers();
    }

    /**
     * Sets up the table columns with appropriate property values.
     */
    private void setupTableColumns() {
        voucherNoColumn.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("totalDebit"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("totalCredit"));
    }

    /**
     * Sets default date range for the date pickers.
     */
    private void setDefaultDates() {
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up action handlers for buttons.
     */
    private void setupButtonActions() {
        searchButton.setOnAction(e -> handleSearch());
        exportButton.setOnAction(e -> handleExport());
    }

    /**
     * Loads vouchers based on the selected date range.
     */
    private void loadVouchers() {
        try {
            List<Voucher> vouchers = voucherService.searchVouchers(
                "", // empty keyword to get all vouchers
                startDatePicker.getValue(),
                endDatePicker.getValue()
            );
            voucherTable.getItems().setAll(vouchers);
            updateStatusLabel(vouchers.size());
        } catch (Exception e) {
            showError("Failed to load vouchers: " + e.getMessage());
        }
    }

    /**
     * Handles the search action.
     * Reloads vouchers with the selected date range.
     */
    private void handleSearch() {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showError("Please select both start and end dates");
            return;
        }
        loadVouchers();
    }

    /**
     * Handles the export action.
     * Exports the current voucher list to a file.
     */
    private void handleExport() {
        try {
            // Create a temporary file for export
            String filename = "vouchers_" + LocalDate.now().toString() + ".csv";
            List<Voucher> vouchers = voucherTable.getItems();
            voucherService.exportVouchers(vouchers, filename);
            showSuccess("Vouchers exported successfully to " + filename);
        } catch (Exception e) {
            showError("Failed to export vouchers: " + e.getMessage());
        }
    }

    /**
     * Updates the status label with the number of vouchers.
     * @param count The number of vouchers displayed
     */
    private void updateStatusLabel(int count) {
        statusLabel.setText("Showing " + count + " vouchers");
    }

    /**
     * Shows an error message in the status label.
     * @param message The error message to display
     */
    private void showError(String message) {
        statusLabel.setText("Error: " + message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    /**
     * Shows a success message in the status label.
     * @param message The success message to display
     */
    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    private void handleEdit(Voucher voucher) {
        if (voucher != null) {
            // Open voucher edit window
            // VoucherEntryDialog.show(voucher);
            loadVouchers(); // Refresh after edit
        }
    }
    
    private void handleDelete(Voucher voucher) {
        if (voucher != null) {
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete voucher " + voucher.getVoucherNo() + "?",
                ButtonType.YES, ButtonType.NO).showAndWait();
                
            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    voucherService.deleteVoucher(voucher.getId());
                    loadVouchers(); // Refresh after delete
                    new Alert(Alert.AlertType.INFORMATION, 
                        "Voucher deleted successfully").show();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR,
                        "Failed to delete: " + e.getMessage()).show();
                }
            }
        }
    }
    
    @FXML
    private void handleNew() {
        // Open new voucher entry window
        // VoucherEntryDialog.show();
        loadVouchers(); // Refresh after new entry
    }

    @FXML
    private void handleDelete() {
        Voucher selectedVoucher = voucherTable.getSelectionModel().getSelectedItem();
        if (selectedVoucher != null) {
            if (Utils.showConfirmation("Confirm Delete",
                "Are you sure you want to delete voucher " + selectedVoucher.getVoucherNo() + "?")) {
                voucherService.deleteVoucher(selectedVoucher.getId());
                loadVouchers();
                Utils.showInfo("Success", "Voucher deleted successfully");
            }
        } else {
            Utils.showError("Error", "Please select a voucher to delete");
        }
    }
} 