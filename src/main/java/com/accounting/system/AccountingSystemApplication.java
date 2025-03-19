package com.accounting.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AccountingSystemApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the main layout
        BorderPane mainLayout = new BorderPane();

        // Create menu bar
        MenuBar menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        // Create left sidebar with main functions
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);

        // Create main content area
        TabPane contentArea = createContentArea();
        mainLayout.setCenter(contentArea);

        // Create the scene
        Scene scene = new Scene(mainLayout, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Accounting System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(
                new MenuItem("New"),
                new MenuItem("Open"),
                new MenuItem("Save"),
                new SeparatorMenuItem(),
                new MenuItem("Exit")
        );

        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(
                new MenuItem("Undo"),
                new MenuItem("Redo")
        );

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(
                new MenuItem("About")
        );

        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        return menuBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.getStyleClass().add("sidebar");

        Button[] buttons = {
                new Button("Voucher Management"),
                new Button("Ledger Management"),
                new Button("Period-End Closing"),
                new Button("Financial Reports"),
                new Button("Basic Settings")
        };

        for (Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            sidebar.getChildren().add(button);
        }

        return sidebar;
    }

    private TabPane createContentArea() {
        TabPane tabPane = new TabPane();

        // Create some sample tabs
        Tab voucherTab = new Tab("Voucher List");
        voucherTab.setContent(createVoucherContent());

        Tab ledgerTab = new Tab("General Ledger");
        ledgerTab.setContent(new VBox());

        tabPane.getTabs().addAll(voucherTab, ledgerTab);
        return tabPane;
    }

    private VBox createVoucherContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("add");

        Button editButton = new Button("Edit");
        editButton.getStyleClass().add("edit");

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("delete");

        Button verifyButton = new Button("Verify");
        verifyButton.getStyleClass().add("verify");

        // Toolbar
        ToolBar toolbar = new ToolBar(addButton, editButton, deleteButton, verifyButton);

        // Table
        TableView<Object> table = new TableView<>();
        table.getColumns().addAll(
                new TableColumn<>("Voucher No."),
                new TableColumn<>("Date"),
                new TableColumn<>("Description"),
                new TableColumn<>("Debit Amount"),
                new TableColumn<>("Credit Amount"),
                new TableColumn<>("Status")
        );
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setPlaceholder(new Label("No content in the table."));

        content.getChildren().addAll(toolbar, table);
        return content;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
