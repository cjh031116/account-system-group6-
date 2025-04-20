package com.accounting.system.component;

import com.accounting.system.model.Account;
import com.accounting.system.service.AccountService;
import com.accounting.system.service.impl.AccountServiceImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AccountSelector extends VBox {
    private final TreeView<Account> accountTree;
    private final TextField searchField;
    private final ListView<Account> recentAccounts;
    private final ObjectProperty<Account> selectedAccount;
    private final AccountService accountService;
    private final ObservableList<Account> recentAccountsList;

    public AccountSelector() {
        this.accountService = new AccountServiceImpl();
        this.selectedAccount = new SimpleObjectProperty<>();
        this.recentAccountsList = FXCollections.observableArrayList();

        // Create search field
        searchField = new TextField();
        searchField.setPromptText("Search accounts...");
        searchField.textProperty().addListener((obs, old, newVal) -> 
            filterAccounts(newVal));

        // Create account tree
        accountTree = new TreeView<>();
        accountTree.setShowRoot(false);
        accountTree.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                } else {
                    setText(account.getAccountCode() + " - " + account.getAccountName());
                }
            }
        });
        accountTree.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, newVal) -> {
                if (newVal != null) {
                    Account selected = newVal.getValue();
                    if (!selected.getIsGroup()) {
                        selectedAccount.set(selected);
                        addToRecentAccounts(selected);
                    }
                }
            }
        );

        // Create recent accounts list
        recentAccounts = new ListView<>(recentAccountsList);
        recentAccounts.setPrefHeight(100);
        recentAccounts.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                } else {
                    setText(account.getAccountCode() + " - " + account.getAccountName());
                }
            }
        });
        recentAccounts.setOnMouseClicked(e -> {
            Account selected = recentAccounts.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedAccount.set(selected);
            }
        });

        // Layout
        Label recentLabel = new Label("Recently Used");
        getChildren().addAll(searchField, accountTree, recentLabel, recentAccounts);
        setSpacing(5);

        // Initial load
        loadAccounts();
        loadRecentAccounts();
    }

    private void loadAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        TreeItem<Account> root = buildAccountTree(accounts);
        accountTree.setRoot(root);
    }

} 