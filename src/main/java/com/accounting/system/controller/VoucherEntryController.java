package com.accounting.system.controller;

import com.accounting.system.common.Utils;
import com.accounting.system.model.Account;
import com.accounting.system.model.Voucher;
import com.accounting.system.model.VoucherEntry;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.AccountServiceImpl;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class VoucherEntryController {
    @FXML private TextField voucherNoField;
    @FXML private DatePicker datePicker;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<Account> accountComboBox;
    @FXML private TextField entryDescriptionField;
    @FXML private TextField debitField;
    @FXML private TextField creditField;
    @FXML private TableView<VoucherEntry> entriesTable;
    @FXML private TableColumn<VoucherEntry, String> accountCodeColumn;
    @FXML private TableColumn<VoucherEntry, String> accountNameColumn;
    @FXML private TableColumn<VoucherEntry, String> descriptionColumn;
    @FXML private TableColumn<VoucherEntry, Double> debitColumn;
    @FXML private TableColumn<VoucherEntry, Double> creditColumn;
    @FXML private TableColumn<VoucherEntry, Void> actionColumn;
    @FXML private Label totalDebitLabel;
    @FXML private Label totalCreditLabel;

    private final VoucherService voucherService;
    private final AccountService accountService;
    private final ObservableList<VoucherEntry> entries = FXCollections.observableArrayList();
    private Voucher currentVoucher;

    public VoucherEntryController() {
        this.voucherService = new VoucherServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    @FXML
    private void initialize() {
        setupControls();
        setupTable();
        initializeNewVoucher();
    }

    private void setupControls() {
        // Setup DatePicker
        datePicker.setValue(LocalDate.now());

        // Setup AccountComboBox
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

        // Setup amount fields
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

} 