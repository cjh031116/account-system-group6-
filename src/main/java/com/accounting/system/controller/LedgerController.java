package com.accounting.system.controller;

import com.accounting.system.common.Utils;
import com.accounting.system.model.Account;
import com.accounting.system.model.VoucherEntry;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.AccountServiceImpl;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class for the ledger view.
 * Handles displaying and managing account transactions and balances.
 */
public class LedgerController {
    // Service dependencies
    private final AccountService accountService; // Service for account-related operations
    private final VoucherService voucherService; // Service for voucher-related operations
    
    // UI Components
    @FXML private ComboBox<Account> accountComboBox; // Dropdown for selecting accounts
    @FXML private DatePicker startDatePicker; // Date picker for start date
    @FXML private DatePicker endDatePicker; // Date picker for end date
    @FXML private Button searchButton; // Button to initiate search
    @FXML private Button exportButton; // Button to export ledger data
    
    // Table columns
    @FXML private TableView<VoucherEntry> ledgerTable; // Table to display ledger entries
    @FXML private TableColumn<VoucherEntry, LocalDate> dateColumn; // Column for transaction date
    @FXML private TableColumn<VoucherEntry, String> voucherNoColumn; // Column for voucher number
    @FXML private TableColumn<VoucherEntry, String> descriptionColumn; // Column for transaction description
    @FXML private TableColumn<VoucherEntry, Double> debitColumn; // Column for debit amount
    @FXML private TableColumn<VoucherEntry, Double> creditColumn; // Column for credit amount
    @FXML private TableColumn<VoucherEntry, Double> balanceColumn; // Column for running balance
    
    // Summary labels
    @FXML private Label openingBalanceLabel; // Label for opening balance
    @FXML private Label totalDebitLabel; // Label for total debit
    @FXML private Label totalCreditLabel; // Label for total credit
    @FXML private Label closingBalanceLabel; // Label for closing balance

    /**
     * Default constructor initializes the controller with required services.
     */
    public LedgerController() {
        this.accountService = new AccountServiceImpl();
        this.voucherService = new VoucherServiceImpl();
    }

    /**
     * Initializes the controller after FXML loading.
     * Sets up UI components and event handlers.
     */
    @FXML
    public void initialize() {
        // Initialize account combo box
        initializeAccountComboBox();
        
        // Set up table columns
        setupTableColumns();
        
        // Set default dates
        setDefaultDates();
        
        // Set up button actions
        setupButtonActions();
    }

    /**
     * Initializes the account combo box with all active accounts.
     */
    private void initializeAccountComboBox() {
        List<Account> accounts = accountService.getActiveAccounts();
        accountComboBox.getItems().addAll(accounts);
        accountComboBox.setCellFactory(lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getCode() + " - " + item.getName());
            }
        });
    }

    /**
     * Sets up the table columns with appropriate property value factories.
     */
    private void setupTableColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        voucherNoColumn.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("debit"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    /**
     * Sets default date values for the date pickers.
     */
    private void setDefaultDates() {
        startDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        endDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up action handlers for buttons.
     */
    private void setupButtonActions() {
        searchButton.setOnAction(event -> handleSearch());
        exportButton.setOnAction(event -> handleExport());
    }

    /**
     * Handles the search action when the search button is clicked.
     * Loads and displays ledger entries based on selected criteria.
     */
    private void handleSearch() {
        Account selectedAccount = accountComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        if (selectedAccount == null) {
            Utils.showError("Search Error", "Please select an account");
            return;
        }
        
        if (startDate == null || endDate == null) {
            Utils.showError("Search Error", "Please select both start and end dates");
            return;
        }
        
        if (startDate.isAfter(endDate)) {
            Utils.showError("Search Error", "Start date cannot be after end date");
            return;
        }
        
        // TODO: Implement loading and displaying ledger entries
        updateSummaryLabels(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Handles the export action when the export button is clicked.
     * Exports the current ledger view to a file.
     */
    private void handleExport() {
        // TODO: Implement export functionality
        Utils.showInfo("Export", "Export functionality not yet implemented");
    }

    /**
     * Updates the summary labels with calculated values.
     * @param openingBalance Opening balance amount
     * @param totalDebit Total debit amount
     * @param totalCredit Total credit amount
     * @param closingBalance Closing balance amount
     */
    private void updateSummaryLabels(double openingBalance, double totalDebit, 
                                   double totalCredit, double closingBalance) {
        openingBalanceLabel.setText(String.format("%,.2f", openingBalance));
        totalDebitLabel.setText(String.format("%,.2f", totalDebit));
        totalCreditLabel.setText(String.format("%,.2f", totalCredit));
        closingBalanceLabel.setText(String.format("%,.2f", closingBalance));
    }
} 