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
import javafx.util.StringConverter;

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

    private TreeItem<Account> buildAccountTree(List<Account> accounts) {
        TreeItem<Account> root = new TreeItem<>();
        
        // First pass: create all nodes
        for (Account account : accounts) {
            TreeItem<Account> item = new TreeItem<>(account);
            if (account.getParentAccountCode() == null) {
                root.getChildren().add(item);
            }
        }
        
        // Second pass: establish parent-child relationships
        for (Account account : accounts) {
            if (account.getParentAccountCode() != null) {
                TreeItem<Account> parentItem = findTreeItem(root, account.getParentAccountCode());
                if (parentItem != null) {
                    parentItem.getChildren().add(new TreeItem<>(account));
                }
            }
        }
        
        return root;
    }

    private TreeItem<Account> findTreeItem(TreeItem<Account> root, String accountCode) {
        if (root.getValue() != null && accountCode.equals(root.getValue().getAccountCode())) {
            return root;
        }
        
        for (TreeItem<Account> child : root.getChildren()) {
            TreeItem<Account> result = findTreeItem(child, accountCode);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }

    private void filterAccounts(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadAccounts();
            return;
        }

        List<Account> allAccounts = accountService.getAllAccounts();
        List<Account> filteredAccounts = new ArrayList<>();
        
        for (Account account : allAccounts) {
            if (account.getAccountCode().contains(searchText) ||
                account.getAccountName().contains(searchText)) {
                filteredAccounts.add(account);
            }
        }

        TreeItem<Account> filteredRoot = new TreeItem<>();
        for (Account account : filteredAccounts) {
            filteredRoot.getChildren().add(new TreeItem<>(account));
        }
        
        accountTree.setRoot(filteredRoot);
    }

    private void loadRecentAccounts() {
        List<Account> recent = accountService.getRecentAccounts();
        recentAccountsList.setAll(recent);
    }

    private void addToRecentAccounts(Account account) {
        if (!recentAccountsList.contains(account)) {
            recentAccountsList.add(0, account);
            if (recentAccountsList.size() > 5) {
                recentAccountsList.remove(5, recentAccountsList.size());
            }
            accountService.addRecentAccount(account);
        }
    }

    public Account getSelectedAccount() {
        return selectedAccount.get();
    }

    public ObjectProperty<Account> selectedAccountProperty() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account account) {
        selectedAccount.set(account);
    }
} 