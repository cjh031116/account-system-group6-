package com.accounting.system.controller;

import com.accounting.system.common.Constants;
import com.accounting.system.common.Utils;
import com.accounting.system.model.Voucher;
import com.accounting.system.model.User;
import com.accounting.system.service.UserService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.UserServiceImpl;
import com.accounting.system.service.impl.VoucherServiceImpl;
import com.accounting.system.service.impl.AccountServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainController {
    @FXML
    private TableView<Voucher> voucherTable;
    @FXML
    private TableColumn<Voucher, String> voucherNoColumn;
    @FXML
    private TableColumn<Voucher, String> dateColumn;
    @FXML
    private TableColumn<Voucher, String> descriptionColumn;
    @FXML
    private TableColumn<Voucher, String> statusColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button verifyButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Pagination pagination;
    @FXML
    private Label welcomeLabel;

    private final UserService userService;
    private final VoucherService voucherService;
    private final ObservableList<Voucher> vouchers;
    private static final int ITEMS_PER_PAGE = 10;
    private User currentUser;

    public MainController() {
        this.userService = new UserServiceImpl();
        this.voucherService = new VoucherServiceImpl();
        this.vouchers = FXCollections.observableArrayList();
    }

    public MainController(UserService userService, VoucherService voucherService) {
        this.userService = userService;
        this.voucherService = voucherService;
        this.vouchers = FXCollections.observableArrayList();
    }

    public void initData(User user) {
        this.currentUser = user;
    }

    @FXML
    private void initialize() {
        setupTableColumns();
        setupPagination();
        setupSearch();
        loadVouchers();
        setupButtonActions();
    }

    private void setupTableColumns() {
        voucherNoColumn.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDate()
                .format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupPagination() {
        pagination.setPageCount(calculatePageCount());
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            int startIndex = newVal.intValue() * ITEMS_PER_PAGE;
            voucherTable.setItems(FXCollections.observableArrayList(
                vouchers.subList(startIndex, Math.min(startIndex + ITEMS_PER_PAGE, vouchers.size()))
            ));
        });
    }

    private int calculatePageCount() {
        return (int) Math.ceil((double) vouchers.size() / ITEMS_PER_PAGE);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterVouchers();
        });

        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterVouchers();
        });

        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterVouchers();
        });
    }

    private void filterVouchers() {
        String keyword = searchField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        ObservableList<Voucher> filteredVouchers = FXCollections.observableArrayList(
            voucherService.searchVouchers(keyword, startDate, endDate)
        );
        
        voucherTable.setItems(filteredVouchers);
        pagination.setPageCount(calculatePageCount());
    }

    private void loadVouchers() {
        vouchers.setAll(voucherService.getAllVouchers());
        voucherTable.setItems(vouchers);
        pagination.setPageCount(calculatePageCount());
    }

    private void setupButtonActions() {
        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        verifyButton.setOnAction(e -> handleVerify());
        logoutButton.setOnAction(e -> handleLogout());
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/voucher_entry.fxml"));
            Parent root = loader.load();
            
            VoucherEntryController controller = loader.getController();
            controller.setVoucherService(voucherService);
            controller.setAccountService(new AccountServiceImpl());
            
            Stage stage = new Stage();
            stage.setTitle("Add New Voucher");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            loadVouchers();
        } catch (IOException e) {
            System.err.println("Error loading voucher_entry.fxml: " + e.getMessage());
            e.printStackTrace();
            Utils.showError("Error", "Failed to open voucher entry form: " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        Voucher selectedVoucher = voucherTable.getSelectionModel().getSelectedItem();
        if (selectedVoucher != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.VOUCHER_ENTRY_FXML));
                Parent root = loader.load();
                
                VoucherEntryController controller = loader.getController();
                controller.setVoucherService(voucherService);
                controller.setVoucher(selectedVoucher);
                
                Stage stage = new Stage();
                stage.setTitle("Edit Voucher");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                
                loadVouchers();
            } catch (IOException e) {
                Utils.showError("Error", "Failed to open voucher entry form: " + e.getMessage());
            }
        } else {
            Utils.showError("Error", "Please select a voucher to edit");
        }
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

    @FXML
    private void handleVerify() {
        Voucher selectedVoucher = voucherTable.getSelectionModel().getSelectedItem();
        if (selectedVoucher != null) {
            if (voucherService.isBalanced(selectedVoucher)) {
                voucherService.verifyVoucher(selectedVoucher.getId(), 1L); // TODO: Use actual user ID
                loadVouchers();
                Utils.showInfo("Success", "Voucher verified successfully");
            } else {
                Utils.showError("Error", "Cannot verify unbalanced voucher");
            }
        } else {
            Utils.showError("Error", "Please select a voucher to verify");
        }
    }

    @FXML
    private void handleLogout() {
        if (Utils.showConfirmation("Confirm Logout", "Are you sure you want to logout?")) {
            userService.endSession();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FXML));
                Parent root = loader.load();
                
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.setTitle(Constants.APP_TITLE + " - Login");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                Utils.showError("Error", "Failed to logout: " + e.getMessage());
            }
        }
    }
} 