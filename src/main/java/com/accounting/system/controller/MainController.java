package com.accounting.system.controller;

import com.accounting.system.common.Constants;
import com.accounting.system.common.Utils;
import com.accounting.system.model.User;
import com.accounting.system.model.Voucher;
import com.accounting.system.service.AiService;
import com.accounting.system.service.VoucherService;
import com.accounting.system.service.impl.AccountServiceImpl;
import com.accounting.system.service.impl.AiServiceImpl;
import com.accounting.system.service.impl.VoucherServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Main controller class for the application.
 * Handles the main application window, navigation, and user session management.
 */
public class MainController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu viewMenu;
    @FXML
    private Menu helpMenu;
//    @FXML
//    private Button voucherEntryButton;
//    @FXML
//    private Button voucherListButton;
//    @FXML
//    private Button ledgerButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button importButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button verifyButton;
    @FXML
    private TableView<Voucher> voucherTable;
    @FXML
    private TableColumn<Voucher, String> voucherNoColumn;
    @FXML
    private TableColumn<Voucher, String> dateColumn;
    @FXML
    private TableColumn<Voucher, String> descriptionColumn;
    @FXML
    private TableColumn<Voucher, String> categoryColumn;
    @FXML
    private TableColumn<Voucher, String> directionColumn;
    @FXML
    private TableColumn<Voucher, Double> debitAmountColumn;
    @FXML
    private TableColumn<Voucher, Double> creditAmountColumn;
    @FXML
    private TableColumn<Voucher, String> statusColumn;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button financialReportsButton;
    @FXML
    private Button aiConsultingButton;

    private final VoucherService voucherService;
    private final AiService aiService;
    private final ObservableList<Voucher> vouchers;
    private User currentUser;

    private static final double MAX_EXPENSE_TO_ALERT = 1000;

    public MainController() {
        this.voucherService = new VoucherServiceImpl();
        this.aiService = new AiServiceImpl();
        this.vouchers = FXCollections.observableArrayList();
    }

    /**
     * Initializes the controller with user data.
     * @param user The logged in user
     */
    public void initData(User user) {
        this.currentUser = user;
        setupWelcomeLabel();
        setupNavigationButtons();
        setupLogoutButton();
    }

    private void setupWelcomeLabel() {
        String username = currentUser.getUsername();
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    private void setupNavigationButtons() {
//        voucherEntryButton.setOnAction(event -> loadVoucherEntry());
//        voucherListButton.setOnAction(event -> loadVoucherList());
//        ledgerButton.setOnAction(event -> loadLedger());
        financialReportsButton.setOnAction(event -> loadFinancialReports());
        aiConsultingButton.setOnAction(event -> loadAiConsulting());
    }

    private void setupLogoutButton() {
        logoutButton.setOnAction(event -> handleLogout());
    }

    @FXML
    private void handleMenuAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuText = menuItem.getText();
        
        switch (menuText) {
            case "New Voucher":
                loadVoucherEntry();
                break;
            case "Exit":
                handleLogout();
                break;
            case "About":
                showAboutDialog();
                break;
            case "Refresh":
                loadVouchers();
                break;
            default:
                System.out.println("Menu item clicked: " + menuText);
        }
    }

    /**
     * Loads the voucher entry screen.
     */
    private void loadVoucherEntry() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/voucher_entry.fxml"));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load voucher entry screen");
        }
    }

    /**
     * Loads the voucher list screen.
     */
    private void loadVoucherList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/voucher_list.fxml"));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load voucher list screen");
        }
    }

    /**
     * Loads the ledger screen.
     */
    private void loadLedger() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ledger.fxml"));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load ledger screen");
        }
    }

    private void loadFinancialReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"));
            Parent root = loader.load();

            ReportController controller = loader.getController();
            controller.setVoucherService(voucherService);

            Stage stage = new Stage();
            stage.setTitle("Financial Reports");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();
            stage.showAndWait();

            loadVouchers();
        } catch (IOException e) {
            System.err.println("Error loading voucher_entry.fxml: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to open voucher entry form");
        }
    }

    private void loadAiConsulting() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ai.fxml"));
            Parent root = loader.load();

            AiController controller = loader.getController();
            controller.setAiService(new AiServiceImpl());
            controller.setVoucherService(new VoucherServiceImpl());

            Stage stage = new Stage();
            stage.setTitle("AI Consulting");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();
            stage.showAndWait();

            loadVouchers();
        } catch (IOException e) {
            System.err.println("Error loading voucher_entry.fxml: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to open voucher entry form");
        }
    }

    /**
     * Handles the logout process.
     * Returns to the login screen.
     */
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(Constants.APP_LOGIN_TITLE);
            
            // setting the window size
            stage.setWidth(500);
            stage.setHeight(400);
            
            // center the window on the screen
            stage.centerOnScreen();
            
            // setting the minimum window size
            stage.setMinWidth(500);
            stage.setMinHeight(400);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load login screen");
        }
    }

    /**
     * Shows the about dialog.
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(Constants.APP_TITLE);
        alert.setContentText("Version 4.0\n\nA simple accounting system for managing vouchers.");
        alert.showAndWait();
    }

    /**
     * Shows the help dialog.
     */
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Help");
        alert.setContentText("For assistance, please contact the system administrator.");
        alert.showAndWait();
    }

    /**
     * Shows an error dialog with the given message.
     * @param message The error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        setupTableColumns();
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
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        debitAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalDebit"));
        creditAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalCredit"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
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
    }

    private void loadVouchers() {
        vouchers.setAll(voucherService.getAllVouchers());
        voucherTable.setItems(vouchers);
        double totalExpense = vouchers.stream().filter(v -> "Expense".equals(v.getDirection())).mapToDouble(Voucher::getTotalDebit).sum();
        if (totalExpense > MAX_EXPENSE_TO_ALERT) {
            Utils.showError("Error", "Total Expense Exceeded 1000");
        }
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            uploadFileToServer(selectedFile);
            loadVouchers();
        }
    }

    private void uploadFileToServer(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = reader.readLine()) != null) {
                String[] ss = s.split(",");
                Voucher voucher = new Voucher();
                voucher.setDate(LocalDate.parse(ss[0]));
                voucher.setMonth(ss[0].substring(0, 7));
                voucher.setDescription(ss[1]);
                String category = ss[2];
                if (category != null && !category.isBlank()) {
                    voucher.setCategory(category);
                } else {
                    voucher.setCategory(aiService.category(voucher.getDescription()));
                }
                voucher.setDirection(ss[3]);
                voucher.setTotalDebit(Double.parseDouble(ss[4]));
                voucher.setTotalCredit(voucher.getTotalDebit());
                voucherService.saveVoucher(voucher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();
            stage.showAndWait();
            
            loadVouchers();
        } catch (IOException e) {
            System.err.println("Error loading voucher_entry.fxml: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to open voucher entry form");
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
                showError("Failed to open voucher entry form");
            }
        } else {
            showError("Please select a voucher to edit");
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
                showInfo("Success", "Voucher deleted successfully");
            }
        } else {
            showError("Please select a voucher to delete");
        }
    }

    @FXML
    private void handleVerify() {
        Voucher selectedVoucher = voucherTable.getSelectionModel().getSelectedItem();
        if (selectedVoucher != null) {
            if (voucherService.isBalanced(selectedVoucher)) {
                voucherService.verifyVoucher(selectedVoucher.getId(), currentUser.getId());
                loadVouchers();
                showInfo("Success", "Voucher verified successfully");
            } else {
                showError("Cannot verify unbalanced voucher");
            }
        } else {
            showError("Please select a voucher to verify");
        }
    }

    /**
     * Sets up action handlers for buttons.
     */
    private void setupButtonActions() {
        importButton.setOnAction(e -> handleImport());
        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        verifyButton.setOnAction(e -> handleVerify());
    }

    /**
     * Shows an information dialog with the given title and message.
     * @param title The dialog title
     * @param message The message to display
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 