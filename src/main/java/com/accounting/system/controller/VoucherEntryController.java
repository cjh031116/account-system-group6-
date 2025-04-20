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

    private void setupTable() {
        accountCodeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAccountCode()));
        accountNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAccountName()));
        descriptionColumn.setCellValueFactory(
            new PropertyValueFactory<>("description"));
        debitColumn.setCellValueFactory(
            new PropertyValueFactory<>("debit"));
        creditColumn.setCellValueFactory(
            new PropertyValueFactory<>("credit"));

        // Setup action column
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("删除");

            {
                deleteButton.setOnAction(event -> {
                    VoucherEntry entry = getTableView().getItems().get(getIndex());
                    entries.remove(entry);
                    updateTotals();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        entriesTable.setItems(entries);
    }

    private void initializeNewVoucher() {
        currentVoucher = new Voucher();
        voucherNoField.setText(voucherService.generateVoucherNumber());
        datePicker.setValue(LocalDate.now());
        descriptionField.clear();
        entries.clear();
        updateTotals();
    }

    @FXML
    private void handleAddEntry() {
        Account selectedAccount = accountComboBox.getValue();
        if (selectedAccount == null) {
            Utils.showError("错误", "请选择科目");
            return;
        }

        String description = entryDescriptionField.getText().trim();
        if (description.isEmpty()) {
            Utils.showError("错误", "请输入摘要");
            return;
        }

        double debit = parseAmount(debitField.getText());
        double credit = parseAmount(creditField.getText());

        if (debit == 0 && credit == 0) {
            Utils.showError("错误", "借方或贷方金额必须大于0");
            return;
        }

        if (debit > 0 && credit > 0) {
            Utils.showError("错误", "借方和贷方金额不能同时大于0");
            return;
        }

        VoucherEntry entry = new VoucherEntry(
            selectedAccount.getId(),
            selectedAccount.getCode(),
            selectedAccount.getName(),
            debit,
            credit
        );
        entry.setDescription(description);

        entries.add(entry);
        clearEntryForm();
        updateTotals();
    }

    private void clearEntryForm() {
        accountComboBox.setValue(null);
        entryDescriptionField.clear();
        debitField.clear();
        creditField.clear();
    }

    private double parseAmount(String text) {
        try {
            return text.isEmpty() ? 0 : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateTotals() {
        double totalDebit = entries.stream().mapToDouble(VoucherEntry::getDebit).sum();
        double totalCredit = entries.stream().mapToDouble(VoucherEntry::getCredit).sum();
        
        totalDebitLabel.setText(String.format("%.2f", totalDebit));
        totalCreditLabel.setText(String.format("%.2f", totalCredit));
    }

    @FXML
    private void handleSave() {
        if (entries.isEmpty()) {
            Utils.showError("错误", "请至少添加一条分录");
            return;
        }

        if (!isBalanced()) {
            Utils.showError("错误", "借贷不平衡");
            return;
        }

        currentVoucher.setDate(datePicker.getValue());
        currentVoucher.setDescription(descriptionField.getText().trim());
        currentVoucher.setEntries(entries);
        
        try {
            if (currentVoucher.getId() == null) {
                voucherService.createVoucher(currentVoucher);
            } else {
                voucherService.updateVoucher(currentVoucher);
            }
            Utils.showInfo("成功", "保存成功");
            initializeNewVoucher();
        } catch (Exception e) {
            Utils.showError("错误", "保存失败：" + e.getMessage());
        }
    }

    private boolean isBalanced() {
        double totalDebit = entries.stream().mapToDouble(VoucherEntry::getDebit).sum();
        double totalCredit = entries.stream().mapToDouble(VoucherEntry::getCredit).sum();
        return Math.abs(totalDebit - totalCredit) < 0.01;
    }

    @FXML
    private void handleClear() {
        if (Utils.showConfirmation("确认", "确定要清空所有数据吗？")) {
            initializeNewVoucher();
        }
    }

    @FXML
    private void handleClose() {
        if (!entries.isEmpty()) {
            if (!Utils.showConfirmation("确认", "有未保存的数据，确定要关闭吗？")) {
                return;
            }
        }
        Stage stage = (Stage) voucherNoField.getScene().getWindow();
        stage.close();
    }

    public void setVoucher(Voucher voucher) {
        this.currentVoucher = voucher;
        voucherNoField.setText(voucher.getVoucherNo());
        datePicker.setValue(voucher.getDate());
        descriptionField.setText(voucher.getDescription());
        entries.setAll(voucher.getEntries());
        updateTotals();
    }
} 