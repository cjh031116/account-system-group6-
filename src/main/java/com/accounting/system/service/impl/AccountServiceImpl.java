package com.accounting.system.service.impl;

import com.accounting.system.model.Account;
import com.accounting.system.service.AccountService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private final List<Account> accounts;
    private final List<Account> recentAccounts;
    private static final int MAX_RECENT_ACCOUNTS = 10;

    public AccountServiceImpl() {
        this.accounts = new ArrayList<>();
        this.recentAccounts = new ArrayList<>();
        initializeSampleAccounts();
    }

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

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    @Override
    public List<Account> getActiveAccounts() {
        return accounts.stream()
                .filter(Account::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public Account getAccountById(Long id) {
        return accounts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account getAccountByCode(String code) {
        return accounts.stream()
                .filter(a -> a.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

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

    @Override
    public void deleteAccount(Long id) {
        accounts.removeIf(a -> a.getId().equals(id));
    }

    @Override
    public List<Account> searchAccounts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return accounts.stream()
                .filter(a -> a.getCode().toLowerCase().contains(lowerKeyword) ||
                           a.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> getChildAccounts(Long parentId) {
        return accounts.stream()
                .filter(a -> parentId.equals(a.getParentId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountCodeUnique(String code, Long excludeId) {
        return accounts.stream()
                .filter(a -> !a.getId().equals(excludeId))
                .noneMatch(a -> a.getCode().equals(code));
    }

    private void addRecentAccount(Account account) {
        recentAccounts.removeIf(a -> a.getId().equals(account.getId()));
        recentAccounts.add(0, account);
        if (recentAccounts.size() > MAX_RECENT_ACCOUNTS) {
            recentAccounts.remove(recentAccounts.size() - 1);
        }
    }
} 