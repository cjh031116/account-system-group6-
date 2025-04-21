package com.accounting.system.component;

import com.accounting.system.model.Account;
import com.accounting.system.service.AccountService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.StringConverter;

import java.util.List;

public class AccountSelector extends ComboBox<Account> {
    private final AccountService accountService;
    private final ObservableList<Account> accounts;

    public AccountSelector(AccountService accountService) {
        this.accountService = accountService;
        this.accounts = FXCollections.observableArrayList();
        
        setupConverter();
        loadAccounts();
    }

    private void setupConverter() {
        setConverter(new StringConverter<>() {
            @Override
            public String toString(Account account) {
                return account != null ? account.getCode() + " - " + account.getName() : "";
            }

            @Override
            public Account fromString(String string) {
                return null;
            }
        });
    }

    private void loadAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();
        accounts.setAll(allAccounts);
        setItems(accounts);
    }

    public void refresh() {
        loadAccounts();
    }

    public Account getSelectedAccount() {
        return getValue();
    }

    public void setSelectedAccount(Account account) {
        setValue(account);
    }
} 