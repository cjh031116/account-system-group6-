package com.accounting.system.controller;

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
        voucherNoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getVoucherNo()));
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getDate()));
        descriptionColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDescription()));
        debitColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getTotalDebit()));
        creditColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getTotalCredit()));
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
            
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
    private void handleRefresh() {
        loadVouchers();
    }
} 