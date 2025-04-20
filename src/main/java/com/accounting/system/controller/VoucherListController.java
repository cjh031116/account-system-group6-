package com.accounting.system.controller;

import com.accounting.system.model.Voucher;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.Optional;

public class VoucherListController {
    @FXML private TableView<Voucher> voucherTable;
    @FXML private TableColumn<Voucher, String> voucherNoColumn;
    @FXML private TableColumn<Voucher, LocalDate> dateColumn;
    @FXML private TableColumn<Voucher, String> descriptionColumn;
    @FXML private TableColumn<Voucher, Double> debitColumn;
    @FXML private TableColumn<Voucher, Double> creditColumn;
    @FXML private TableColumn<Voucher, String> statusColumn;
    @FXML private TableColumn<Voucher, Void> actionsColumn;
    
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    
    private final VoucherService voucherService;
    private final ObservableList<Voucher> vouchers;
    private FilteredList<Voucher> filteredVouchers;
    
    public VoucherListController() {
        this.voucherService = new VoucherServiceImpl();
        this.vouchers = FXCollections.observableArrayList();
    }
    
    @FXML
    private void initialize() {
        setupTable();
        setupFilters();
        loadVouchers();
    }
    
    private void setupTable() {
        // Setup columns
        voucherNoColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getVoucherNo()));
        dateColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getDate().toString()));
        descriptionColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getDescription()));
        debitColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getTotalDebit())));
        creditColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getTotalCredit())));
        statusColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getStatus()));
            
        // Setup action column
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actions = new HBox(5, editButton, deleteButton);
            
            {
                editButton.setOnAction(e -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(e -> handleDelete(getTableRow().getItem()));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actions);
            }
        });
        
        // Enable sorting
        voucherTable.getSortOrder().add(dateColumn);
    }
    
    private void setupFilters() {
        // Setup date filters
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
        
        // Setup status filter
        statusFilter.setItems(FXCollections.observableArrayList(
            "All", "DRAFT", "POSTED", "VERIFIED"
        ));
        statusFilter.setValue("All");
        
        // Setup filtered list
        filteredVouchers = new FilteredList<>(vouchers);
        voucherTable.setItems(filteredVouchers);
        
        // Apply filters when changed
        startDatePicker.valueProperty().addListener((obs, old, newVal) -> applyFilters());
        endDatePicker.valueProperty().addListener((obs, old, newVal) -> applyFilters());
        searchField.textProperty().addListener((obs, old, newVal) -> applyFilters());
        statusFilter.valueProperty().addListener((obs, old, newVal) -> applyFilters());
    }
    
    private void applyFilters() {
        filteredVouchers.setPredicate(voucher -> {
            boolean matchesDate = voucher.getDate().compareTo(startDatePicker.getValue()) >= 0
                && voucher.getDate().compareTo(endDatePicker.getValue()) <= 0;
                
            boolean matchesStatus = statusFilter.getValue().equals("All") 
                || voucher.getStatus().equals(statusFilter.getValue());
                
            boolean matchesSearch = searchField.getText().isEmpty()
                || voucher.getVoucherNo().contains(searchField.getText())
                || voucher.getDescription().toLowerCase()
                    .contains(searchField.getText().toLowerCase());
                    
            return matchesDate && matchesStatus && matchesSearch;
        });
    }
    
    private void loadVouchers() {
        vouchers.setAll(voucherService.getAllVouchers());
        applyFilters();
    }

    
    @FXML
    private void handleNew() {
        // Open new voucher entry window
        // VoucherEntryDialog.show();
        loadVouchers(); // Refresh after new entry
    }
    
    @FXML
    private void handleRefresh() {
        loadVouchers();
    }
} 