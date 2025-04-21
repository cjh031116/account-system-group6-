package com.accounting.system.controller;

import com.accounting.system.model.Account;
import com.accounting.system.model.VoucherEntry;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.VoucherService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.util.List;

public class LedgerController {
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableView<VoucherEntry> entriesTable;
    @FXML
    private TableColumn<VoucherEntry, String> dateColumn;
    @FXML
    private TableColumn<VoucherEntry, String> voucherNoColumn;
    @FXML
    private TableColumn<VoucherEntry, String> descriptionColumn;
    @FXML
    private TableColumn<VoucherEntry, Double> debitColumn;
    @FXML
    private TableColumn<VoucherEntry, Double> creditColumn;
    @FXML
    private TableColumn<VoucherEntry, Double> balanceColumn;
    @FXML
    private Label accountCodeLabel;
    @FXML
    private Label accountNameLabel;
    @FXML
    private Label openingBalanceLabel;
    @FXML
    private Label closingBalanceLabel;

    private final AccountService accountService;
    private final VoucherService voucherService;
    private final ObservableList<VoucherEntry> entries;

    public LedgerController(AccountService accountService, VoucherService voucherService) {
        this.accountService = accountService;
        this.voucherService = voucherService;
        this.entries = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        setupAccountComboBox();
        setupTableColumns();
        setupDatePickers();
    }

    private void setupAccountComboBox() {
        accountComboBox.setItems(FXCollections.observableArrayList(accountService.getAllAccounts()));
        accountComboBox.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account account) {
                return account != null ? account.getCode() + " - " + account.getName() : "";
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });
    }

    private void setupTableColumns() {
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDate().toString()));
        voucherNoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getVoucherNo()));
        descriptionColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDescription()));
        debitColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDebit()));
        creditColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getCredit()));
        balanceColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(calculateBalance(cellData.getValue())));
    }

    private double calculateBalance(VoucherEntry entry) {
        // TODO: Implement running balance calculation
        return 0.0;
    }

    private void setupDatePickers() {
        startDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        endDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSearch() {
        Account account = accountComboBox.getValue();
        if (account == null) {
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        accountCodeLabel.setText(account.getCode());
        accountNameLabel.setText(account.getName());

        // TODO: Implement these methods in VoucherService
        // List<VoucherEntry> entries = voucherService.getEntriesByAccount(account.getId(), startDate, endDate);
        // entriesTable.setItems(FXCollections.observableArrayList(entries));

        // TODO: Implement these methods in AccountService
        // double openingBalance = accountService.getOpeningBalance(account.getId(), startDate);
        // double closingBalance = accountService.getBalance(account.getId(), endDate);
        // openingBalanceLabel.setText(String.format("%.2f", openingBalance));
        // closingBalanceLabel.setText(String.format("%.2f", closingBalance));
    }
} 