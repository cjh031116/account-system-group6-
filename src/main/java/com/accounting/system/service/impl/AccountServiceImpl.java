package com.accounting.system.service.impl;

import com.accounting.system.model.Account;
import com.accounting.system.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the AccountService interface.
 * Provides in-memory storage and management of accounts.
 */
public class AccountServiceImpl implements AccountService {
    // In-memory storage for accounts
    private final List<Account> accounts; // List of all accounts
    private final List<Account> recentAccounts; // List of recently accessed accounts
    private static final int MAX_RECENT_ACCOUNTS = 10; // Maximum number of recent accounts to keep

    /**
     * Constructor initializes the service with sample accounts.
     */
    public AccountServiceImpl() {
        this.accounts = new ArrayList<>();
        this.recentAccounts = new ArrayList<>();
        initializeSampleAccounts();
    }

    /**
     * Initializes the service with sample accounts for testing and demonstration.
     * Creates basic accounts for different account types.
     */
    private void initializeSampleAccounts() {
        // Add sample accounts
        Account cash = createAccount("1001", "Cash", "ASSET", true, null);
        Account bank = createAccount("1002", "Bank", "ASSET", true, null);
        Account ar = createAccount("1100", "Accounts Receivable", "ASSET", true, null);
        Account ap = createAccount("2000", "Accounts Payable", "LIABILITY", true, null);
        Account revenue = createAccount("4000", "Revenue", "REVENUE", true, null);
        Account expense = createAccount("5000", "Expense", "EXPENSE", true, null);
        
        accounts.add(cash);
        accounts.add(bank);
        accounts.add(ar);
        accounts.add(ap);
        accounts.add(revenue);
        accounts.add(expense);
    }

    /**
     * Helper method to create a new account with specified properties.
     * @param code Account code
     * @param name Account name
     * @param type Account type
     * @param active Whether the account is active
     * @param parentId ID of the parent account
     * @return The created account
     */
    private Account createAccount(String code, String name, String type, boolean active, Long parentId) {
        Account account = new Account();
        account.setId((long) accounts.size() + 1);
        account.setCode(code);
        account.setName(name);
        account.setType(type);
        account.setActive(active);
        account.setParentId(parentId);
        return account;
    }

    /**
     * Retrieves all accounts from the system.
     * @return List of all accounts
     */
    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    /**
     * Retrieves all active accounts from the system.
     * @return List of active accounts
     */
    @Override
    public List<Account> getActiveAccounts() {
        return accounts.stream()
                .filter(Account::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an account by its unique identifier.
     * @param id The account ID to search for
     * @return The account if found, null otherwise
     */
    @Override
    public Account getAccountById(Long id) {
        return accounts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves an account by its code.
     * @param code The account code to search for
     * @return The account if found, null otherwise
     */
    @Override
    public Account getAccountByCode(String code) {
        return accounts.stream()
                .filter(a -> a.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a new account in the system.
     * @param account The account to create
     * @return The created account with generated ID
     * @throws IllegalArgumentException if account code already exists
     */
    @Override
    public Account createAccount(Account account) {
        if (!isAccountCodeUnique(account.getCode(), null)) {
            throw new IllegalArgumentException("Account code already exists");
        }
        
        account.setId((long) accounts.size() + 1);
        accounts.add(account);
        addRecentAccount(account);
        return account;
    }

    /**
     * Updates an existing account in the system.
     * @param account The account to update
     * @return The updated account
     * @throws IllegalArgumentException if account code already exists or account not found
     */
    @Override
    public Account updateAccount(Account account) {
        if (!isAccountCodeUnique(account.getCode(), account.getId())) {
            throw new IllegalArgumentException("Account code already exists");
        }
        
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(account.getId())) {
                accounts.set(i, account);
                addRecentAccount(account);
                return account;
            }
        }
        throw new IllegalArgumentException("Account not found");
    }

    /**
     * Deletes an account from the system.
     * @param id The ID of the account to delete
     * @throws IllegalArgumentException if account not found
     */
    @Override
    public void deleteAccount(Long id) {
        accounts.removeIf(a -> a.getId().equals(id));
    }

    /**
     * Searches for accounts matching the given keyword.
     * Searches in both account code and name.
     * @param keyword The search term
     * @return List of matching accounts
     */
    @Override
    public List<Account> searchAccounts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return accounts.stream()
                .filter(a -> a.getCode().toLowerCase().contains(lowerKeyword) ||
                           a.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all child accounts of a parent account.
     * @param parentId The ID of the parent account
     * @return List of child accounts
     */
    @Override
    public List<Account> getChildAccounts(Long parentId) {
        return accounts.stream()
                .filter(a -> parentId.equals(a.getParentId()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if an account code is unique in the system.
     * @param code The account code to check
     * @param excludeId The ID of the account to exclude from the check (for updates)
     * @return true if the code is unique, false otherwise
     */
    @Override
    public boolean isAccountCodeUnique(String code, Long excludeId) {
        return accounts.stream()
                .filter(a -> !a.getId().equals(excludeId))
                .noneMatch(a -> a.getCode().equals(code));
    }

    /**
     * Adds an account to the recent accounts list.
     * Maintains the list size within the maximum limit.
     * @param account The account to add
     */
    private void addRecentAccount(Account account) {
        recentAccounts.removeIf(a -> a.getId().equals(account.getId()));
        recentAccounts.add(0, account);
        if (recentAccounts.size() > MAX_RECENT_ACCOUNTS) {
            recentAccounts.remove(recentAccounts.size() - 1);
        }
    }
} 