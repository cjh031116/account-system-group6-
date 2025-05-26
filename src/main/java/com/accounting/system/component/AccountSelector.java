package com.accounting.system.component;

import com.accounting.system.model.Account;
import com.accounting.system.service.AccountService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.List;

/**
 * Custom component for selecting accounts from a dropdown list.
 * Extends JavaFX ComboBox to provide account selection functionality.11
 */
public class AccountSelector extends ComboBox<Account> {
    // Service for account-related operations
    private final AccountService accountService;
    
    // Observable list to store and manage accounts
    private final ObservableList<Account> accounts;

    /**
     * Constructor initializes the account selector with required service.
     * @param accountService Service for account-related operations
     */
    public AccountSelector(AccountService accountService) {
        this.accountService = accountService;
        this.accounts = FXCollections.observableArrayList();
        
        // Set up the string converter for displaying accounts
        setupConverter();
        // Load initial accounts
        loadAccounts();
    }

    /**
     * Sets up the string converter to display accounts in the format "code - name".
     * Handles conversion between Account objects and their string representation.
     */
    private void setupConverter() {
        setConverter(new StringConverter<>() {
            /**
             * Converts an Account object to its string representation.
             * @param account The account to convert
             * @return String representation of the account in "code - name" format
             */
            @Override
            public String toString(Account account) {
                return account != null ? account.getCode() + " - " + account.getName() : "";
            }

            /**
             * Converts a string back to an Account object.
             * Not implemented as it's not needed for this use case.
             * @param string The string to convert
             * @return null as this conversion is not supported
             */
            @Override
            public Account fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Loads all accounts from the service and populates the selector.
     * Updates the observable list with the latest account data.
     */
    private void loadAccounts() {
        // Get all accounts from the service
        List<Account> allAccounts = accountService.getAllAccounts();
        // Update the observable list
        accounts.setAll(allAccounts);
        // Set the items in the combo box
        setItems(accounts);
    }

    /**
     * Refreshes the account list by reloading from the service.
     * Updates the selector with the latest account data.
     */
    public void refresh() {
        loadAccounts();
    }

    /**
     * Gets the currently selected account.
     * @return The selected account, or null if no account is selected
     */
    public Account getSelectedAccount() {
        return getValue();
    }

    /**
     * Sets the selected account in the selector.
     * @param account The account to select
     */
    public void setSelectedAccount(Account account) {
        setValue(account);
    }
}