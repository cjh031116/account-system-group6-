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
import java.util.List;

public class VoucherEntryController {
    @FXML
    private TextField voucherNoField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TableView<VoucherEntry> entriesTable;
    @FXML
    private TableColumn<VoucherEntry, String> accountColumn;
    @FXML
    private TableColumn<VoucherEntry, String> debitColumn;
    @FXML
    private TableColumn<VoucherEntry, String> creditColumn;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private TextField debitField;
    @FXML
    private TextField creditField;
    @FXML
    private Button addEntryButton;
    @FXML
    private Button removeEntryButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label totalDebitLabel;
    @FXML
    private Label totalCreditLabel;
    @FXML
    private Label balanceLabel;

    private VoucherService voucherService;
    private AccountService accountService;
    private Voucher voucher;
    private final ObservableList<VoucherEntry> entries;
    private final SimpleDoubleProperty totalDebit;
    private final SimpleDoubleProperty totalCredit;

    public VoucherEntryController() {
        this.entries = FXCollections.observableArrayList();
        this.totalDebit = new SimpleDoubleProperty(0);
        this.totalCredit = new SimpleDoubleProperty(0);
        this.voucherService = new VoucherServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    public void setVoucherService(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
        if (voucher != null) {
            loadVoucherData();
        } else {
            setupNewVoucher();
        }
    }

    @FXML
    private void initialize() {
        setupControls();
        setupTable();
        setupBindings();
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

    private void setupTable() {
        accountColumn.setCellValueFactory(cellData -> 
            cellData.getValue().accountProperty().asString());
        debitColumn.setCellValueFactory(cellData -> 
            cellData.getValue().debitProperty().asString("%.2f"));
        creditColumn.setCellValueFactory(cellData -> 
            cellData.getValue().creditProperty().asString("%.2f"));
        
        entriesTable.setItems(entries);
    }

    private void setupBindings() {
        totalDebitLabel.textProperty().bind(totalDebit.asString("%.2f"));
        totalCreditLabel.textProperty().bind(totalCredit.asString("%.2f"));
        
        balanceLabel.textProperty().bind(
            totalDebit.subtract(totalCredit).asString("%.2f")
        );
        
        entries.addListener((javafx.collections.ListChangeListener.Change<? extends VoucherEntry> c) -> {
            updateTotals();
        });
    }

    private void updateTotals() {
        double debit = 0;
        double credit = 0;
        
        for (VoucherEntry entry : entries) {
            debit += entry.getDebit();
            credit += entry.getCredit();
        }
        
        totalDebit.set(debit);
        totalCredit.set(credit);
    }

    private void loadVoucherData() {
        voucherNoField.setText(voucher.getVoucherNo());
        datePicker.setValue(voucher.getDate());
        descriptionArea.setText(voucher.getDescription());
        
        entries.setAll(voucher.getEntries());
        updateTotals();
    }

    private void setupNewVoucher() {
        voucherNoField.setText(voucherService.generateVoucherNumber());
        datePicker.setValue(LocalDate.now());
        descriptionArea.clear();
        entries.clear();
    }

    @FXML
    private void handleAddEntry() {
        Account account = accountComboBox.getValue();
        if (account == null) {
            Utils.showError("Error", "Please select an account");
            return;
        }

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

    @FXML
    private void handleRemoveEntry() {
        VoucherEntry selectedEntry = entriesTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            entries.remove(selectedEntry);
        } else {
            Utils.showError("Error", "Please select an entry to remove");
        }
    }

    @FXML
    private void handleSave() {
        if (entries.isEmpty()) {
            Utils.showError("Error", "Please add at least one entry");
            return;
        }

        if (Math.abs(totalDebit.get() - totalCredit.get()) > 0.01) {
            Utils.showError("Error", "Debit and credit amounts must be equal");
            return;
        }

        if (voucher == null) {
            voucher = new Voucher();
        }

        voucher.setVoucherNo(voucherNoField.getText());
        voucher.setDate(datePicker.getValue());
        voucher.setDescription(descriptionArea.getText());
        voucher.setEntries(new ArrayList<>(entries));
        voucher.setTotalDebit(totalDebit.get());
        voucher.setTotalCredit(totalCredit.get());

        try {
            voucherService.saveVoucher(voucher);
            Utils.showInfo("Success", "Voucher saved successfully");
            closeWindow();
        } catch (Exception e) {
            Utils.showError("Error", "Failed to save voucher: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) saveButton.getScene().getWindow()).close();
    }

    private double parseAmount(String text) {
        try {
            return text.isEmpty() ? 0 : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
} 