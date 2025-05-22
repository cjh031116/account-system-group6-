package com.accounting.system.controller;

import com.accounting.system.common.Utils;
import com.accounting.system.model.Account;
import com.accounting.system.model.Voucher;
import com.accounting.system.model.VoucherEntry;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.AccountServiceImpl;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controller class for managing voucher entries in the accounting system.
 * Handles the creation, editing, and saving of accounting vouchers.
 */
public class VoucherEntryController {
    // UI Components
    @FXML
    private TextField voucherNoField; // Field to display voucher number
    @FXML
    private DatePicker datePicker; // Date picker for voucher date
    @FXML
    private TextArea descriptionArea; // Text area for voucher description
    @FXML
    private TableView<VoucherEntry> entriesTable; // Table to display voucher entries
    @FXML
    private TableColumn<VoucherEntry, String> accountColumn; // Column for account information
    @FXML
    private TableColumn<VoucherEntry, String> debitColumn; // Column for debit amounts
    @FXML
    private TableColumn<VoucherEntry, String> creditColumn; // Column for credit amounts
    @FXML
    private ComboBox<Account> accountComboBox; // Dropdown for account selection
    @FXML
    private TextField debitField; // Field for entering debit amount
    @FXML
    private TextField creditField; // Field for entering credit amount
    @FXML
    private Button addEntryButton; // Button to add new entry
    @FXML
    private Button removeEntryButton; // Button to remove selected entry
    @FXML
    private Button saveButton; // Button to save the voucher
    @FXML
    private Button cancelButton; // Button to cancel voucher creation/editing
    @FXML
    private Label totalDebitLabel; // Label to display total debit amount
    @FXML
    private Label totalCreditLabel; // Label to display total credit amount
    @FXML
    private Label balanceLabel; // Label to display balance (debit - credit)

    // Service dependencies
    private VoucherService voucherService; // Service for voucher operations
    private AccountService accountService; // Service for account operations
    private Voucher voucher; // Current voucher being edited
    private final ObservableList<VoucherEntry> entries; // List of voucher entries
    private final SimpleDoubleProperty totalDebit; // Property for total debit amount
    private final SimpleDoubleProperty totalCredit; // Property for total credit amount

    /**
     * Constructor initializes the controller with default values and services.
     */
    public VoucherEntryController() {
        this.entries = FXCollections.observableArrayList();
        this.totalDebit = new SimpleDoubleProperty(0);
        this.totalCredit = new SimpleDoubleProperty(0);
        this.voucherService = new VoucherServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    /**
     * Sets the voucher service for this controller.
     * @param voucherService The voucher service to be used
     */
    public void setVoucherService(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    /**
     * Sets the account service for this controller.
     * @param accountService The account service to be used
     */
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Sets the voucher to be edited and loads its data.
     * @param voucher The voucher to be edited, null for new voucher
     */
    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
        if (voucher != null) {
            loadVoucherData();
        } else {
            setupNewVoucher();
        }
    }

    /**
     * Initializes the controller and sets up UI components.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    private void initialize() {
        setupControls();
        setupTable();
        setupBindings();
    }

    /**
     * Sets up the initial state of UI controls.
     */
    private void setupControls() {
        // Set current date in date picker
        datePicker.setValue(LocalDate.now());

        // Configure account combo box with active accounts
        accountComboBox.setItems(FXCollections.observableArrayList(accountService.getActiveAccounts()));
        accountComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Account account) {
                return account != null ? account.getCode() + " - " + account.getName() : "";
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });

        // Add input validation for amount fields
        debitField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                debitField.setText(oldVal);
            }
        });

        creditField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d{0,2})?")) {
                creditField.setText(oldVal);
            }
        });
    }

    /**
     * Configures the table view for displaying voucher entries.
     */
    private void setupTable() {
        // Set up cell value factories for each column
        accountColumn.setCellValueFactory(cellData -> 
            cellData.getValue().accountProperty().asString());
        debitColumn.setCellValueFactory(cellData -> 
            cellData.getValue().debitProperty().asString("%.2f"));
        creditColumn.setCellValueFactory(cellData -> 
            cellData.getValue().creditProperty().asString("%.2f"));
        
        // Set the items in the table
        entriesTable.setItems(entries);
    }

    /**
     * Sets up property bindings for total amounts and balance.
     */
    private void setupBindings() {
        // Bind total labels to their respective properties
        totalDebitLabel.textProperty().bind(totalDebit.asString("%.2f"));
        totalCreditLabel.textProperty().bind(totalCredit.asString("%.2f"));
        
        // Bind balance label to the difference between debit and credit
        balanceLabel.textProperty().bind(
            totalDebit.subtract(totalCredit).asString("%.2f")
        );
        
        // Add listener to update totals when entries change
        entries.addListener((javafx.collections.ListChangeListener.Change<? extends VoucherEntry> c) -> {
            updateTotals();
        });
    }

    /**
     * Updates the total debit and credit amounts based on current entries.
     */
    private void updateTotals() {
        double debit = 0;
        double credit = 0;
        
        // Calculate totals from all entries
        for (VoucherEntry entry : entries) {
            debit += entry.getDebit();
            credit += entry.getCredit();
        }
        
        // Update the properties
        totalDebit.set(debit);
        totalCredit.set(credit);
    }

    /**
     * Loads data from an existing voucher into the UI.
     */
    private void loadVoucherData() {
        voucherNoField.setText(voucher.getVoucherNo());
        datePicker.setValue(voucher.getDate());
        descriptionArea.setText(voucher.getDescription());
        
        entries.setAll(voucher.getEntries());
        updateTotals();
    }

    /**
     * Sets up the UI for creating a new voucher.
     */
    private void setupNewVoucher() {
        voucherNoField.setText(voucherService.generateVoucherNumber());
        datePicker.setValue(LocalDate.now());
        descriptionArea.clear();
        entries.clear();
    }

    /**
     * Handles the addition of a new voucher entry.
     * Validates input and adds the entry to the table.
     */
    @FXML
    private void handleAddEntry() {
        // Validate account selection
        Account account = accountComboBox.getValue();
        if (account == null) {
            Utils.showError("Error", "Please select an account");
            return;
        }

        // Parse and validate amounts
        double debit = parseAmount(debitField.getText());
        double credit = parseAmount(creditField.getText());

        if (debit == 0 && credit == 0) {
            Utils.showError("Error", "Please enter either debit or credit amount");
            return;
        }

        if (debit > 0 && credit > 0) {
            Utils.showError("Error", "Cannot have both debit and credit amounts");
            return;
        }

        // Create and add new entry
        VoucherEntry entry = new VoucherEntry();
        entry.setAccount(account);
        entry.setDebit(debit);
        entry.setCredit(credit);

        entries.add(entry);
        
        // Clear input fields
        accountComboBox.setValue(null);
        debitField.clear();
        creditField.clear();
    }

    /**
     * Handles the removal of a selected voucher entry.
     */
    @FXML
    private void handleRemoveEntry() {
        VoucherEntry selectedEntry = entriesTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            entries.remove(selectedEntry);
        } else {
            Utils.showError("Error", "Please select an entry to remove");
        }
    }

    /**
     * Handles the saving of the voucher.
     * Validates data and saves to the database.
     */
    @FXML
    private void handleSave() {
        // Validate entries
        if (entries.isEmpty()) {
            Utils.showError("Error", "Please add at least one entry");
            return;
        }

        // Validate debit and credit balance
        if (Math.abs(totalDebit.get() - totalCredit.get()) > 0.01) {
            Utils.showError("Error", "Debit and credit amounts must be equal");
            return;
        }

        try {
            // Create new voucher if needed
            if (voucher == null) {
                voucher = new Voucher();
            }

            // Set voucher properties
            voucher.setDate(datePicker.getValue());
            voucher.setDescription(descriptionArea.getText());
            voucher.setEntries(new ArrayList<>(entries));
            voucher.setTotalDebit(totalDebit.get());
            voucher.setTotalCredit(totalCredit.get());
            voucher.setStatus("DRAFT");
            voucher.setCreatedDate(LocalDate.now());

            // Save voucher and close window
            voucherService.saveVoucher(voucher);
            Utils.showInfo("Success", "Voucher saved successfully");
            closeWindow();
        } catch (Exception e) {
            Utils.showError("Error", "Failed to save voucher: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the cancellation of voucher creation/editing.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Parses a string amount into a double value.
     * @param text The text to parse
     * @return The parsed amount, or 0 if parsing fails
     */
    private double parseAmount(String text) {
        try {
            return text.isEmpty() ? 0 : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
} 