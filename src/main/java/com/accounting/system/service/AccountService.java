package com.accounting.system.service;

import com.accounting.system.model.Account;

import java.util.List;

/**
 * Service interface for account management.
 * Provides methods for CRUD operations and account-related queries.
 */
public interface AccountService {
    /**
     * Retrieves all accounts from the system.
     * @return List of all accounts
     */
    List<Account> getAllAccounts();

    /**
     * Retrieves all active accounts from the system.
     * @return List of active accounts
     */
    List<Account> getActiveAccounts();

    /**
     * Retrieves an account by its unique identifier.
     * @param id The account ID to search for
     * @return The account if found, null otherwise
     */
    Account getAccountById(Long id);

    /**
     * Retrieves an account by its code.
     * @param code The account code to search for
     * @return The account if found, null otherwise
     */
    Account getAccountByCode(String code);

    /**
     * Creates a new account in the system.
     * @param account The account to create
     * @return The created account with generated ID
     * @throws IllegalArgumentException if account code already exists
     */
    Account createAccount(Account account);

    /**
     * Updates an existing account in the system.
     * @param account The account to update
     * @return The updated account
     * @throws IllegalArgumentException if account code already exists or account not found
     */
    Account updateAccount(Account account);

    /**
     * Deletes an account from the system.
     * @param id The ID of the account to delete
     * @throws IllegalArgumentException if account not found
     */
    void deleteAccount(Long id);

    /**
     * Searches for accounts matching the given keyword.
     * Searches in both account code and name.
     * @param keyword The search term
     * @return List of matching accounts
     */
    List<Account> searchAccounts(String keyword);

    /**
     * Retrieves all child accounts of a parent account.
     * @param parentId The ID of the parent account
     * @return List of child accounts
     */
    List<Account> getChildAccounts(Long parentId);

    /**
     * Checks if an account code is unique in the system.
     * @param code The account code to check
     * @param excludeId The ID of the account to exclude from the check (for updates)
     * @return true if the code is unique, false otherwise
     */
    boolean isAccountCodeUnique(String code, Long excludeId);
} 