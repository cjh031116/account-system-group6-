package com.accounting.system.controller;

import com.accounting.system.component.AccountSelector;
import com.accounting.system.component.DateRangeSelector;
import com.accounting.system.model.Account;
import com.accounting.system.model.VoucherEntry;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.AccountServiceImpl;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LedgerController {
    @FXML private VBox accountSelectorContainer;
    @FXML private VBox dateRangeSelectorContainer;
    @FXML private TableView<VoucherEntry> ledgerTable;
    @FXML private TableColumn<VoucherEntry, String> dateColumn;
    @FXML private TableColumn<VoucherEntry, String> voucherNoColumn;
    @FXML private TableColumn<VoucherEntry, String> descriptionColumn;
    @FXML private TableColumn<VoucherEntry, String> debitColumn;
    @FXML private TableColumn<VoucherEntry, String> creditColumn;
    @FXML private TableColumn<VoucherEntry, String> balanceColumn;
    @FXML private Label accountInfoLabel;
    @FXML private Label openingBalanceLabel;
    @FXML private Label currentBalanceLabel;

    private final AccountSelector accountSelector;
    private final DateRangeSelector dateRangeSelector;
    private final AccountService accountService;
    private final VoucherService voucherService;
    private final ObservableList<VoucherEntry> entries;

    public LedgerController() {
        this.accountSelector = new AccountSelector();
        this.dateRangeSelector = new DateRangeSelector();
        this.accountService = new AccountServiceImpl();
        this.voucherService = new VoucherServiceImpl();
        this.entries = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        // Add selectors to containers
        accountSelectorContainer.getChildren().add(accountSelector);
        dateRangeSelectorContainer.getChildren().add(dateRangeSelector);

        // Setup table columns
        setupTableColumns();

        // Setup listeners
        setupListeners();

        // Initial load
        loadData();
    }

    private void setupTableColumns() {
        dateColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getVoucherId().toString())); // Need to get date from voucher
        voucherNoColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getVoucherId().toString())); // Need to get voucher no
        descriptionColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getDescription()));
        debitColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(formatAmount(data.getValue().getDebitAmount())));
        creditColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(formatAmount(data.getValue().getCreditAmount())));
        balanceColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(calculateRunningBalance(data.getValue())));

        // Enable sorting
        ledgerTable.getSortOrder().add(dateColumn);
    }

    private void setupListeners() {
        // Update data when account or date range changes
        accountSelector.selectedAccountProperty().addListener((obs, old, newVal) -> 
            loadData());
        dateRangeSelector.startDateProperty().addListener((obs, old, newVal) -> 
            loadData());
        dateRangeSelector.endDateProperty().addListener((obs, old, newVal) -> 
            loadData());
    }

    private void loadData() {
        Account selectedAccount = accountSelector.getSelectedAccount();
        LocalDate startDate = dateRangeSelector.getStartDate();
        LocalDate endDate = dateRangeSelector.getEndDate();

        if (selectedAccount == null || startDate == null || endDate == null) {
            return;
        }

        // Update account info
        updateAccountInfo(selectedAccount);

        // Load entries
        List<VoucherEntry> accountEntries = voucherService.getEntriesByAccount(
            selectedAccount.getId(), startDate, endDate);
        entries.setAll(accountEntries);
        ledgerTable.setItems(entries);

        // Calculate and update balances
        updateBalances(selectedAccount, startDate, endDate);
    }

    private void updateAccountInfo(Account account) {
        accountInfoLabel.setText(String.format("%s - %s", 
            account.getAccountCode(), account.getAccountName()));
    }

    private void updateBalances(Account account, LocalDate startDate, LocalDate endDate) {
        BigDecimal openingBalance = accountService.getOpeningBalance(
            account.getId(), startDate);
        BigDecimal currentBalance = accountService.getBalance(
            account.getId(), endDate);

        openingBalanceLabel.setText(formatAmount(openingBalance));
        currentBalanceLabel.setText(formatAmount(currentBalance));
    }

    private String formatAmount(BigDecimal amount) {
        return String.format("%,.2f", amount);
    }

    private String calculateRunningBalance(VoucherEntry entry) {
        // This should calculate the running balance up to this entry
        // Need to implement the actual calculation logic
        return "0.00";
    }

    @FXML
    private void handleExport() {
        // Implement export functionality
    }

    @FXML
    private void handlePrint() {
        // Implement print functionality
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }
} 