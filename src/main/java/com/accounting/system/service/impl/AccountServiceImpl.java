package com.accounting.system.service.impl;

import com.accounting.system.model.Account;
import com.accounting.system.service.AccountService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private final List<Account> accounts = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);
    private final List<Account> recentAccounts = new LinkedList<>();
    private static final int MAX_RECENT_ACCOUNTS = 5;

    @Override
    public Account createAccount(Account account) {
        account.setId(sequence.getAndIncrement());
        accounts.add(account);
        return account;
    }

    @Override
    public void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(account.getId())) {
                accounts.set(i, account);
                break;
            }
        }
    }

    @Override
    public void deleteAccount(Long id) {
        accounts.removeIf(a -> a.getId().equals(id));
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accounts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
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
    public List<Account> searchAccounts(String keyword) {
        return accounts.stream()
                .filter(a -> a.getCode().contains(keyword) || 
                           a.getName().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> getAccountsByType(String type) {
        return accounts.stream()
                .filter(a -> a.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public void updateBalance(Long accountId, double amount) {
        getAccountById(accountId).ifPresent(account -> {
            account.setBalance(account.getBalance() + amount);
            updateAccount(account);
        });
    }

    @Override
    public List<Account> getAccountTree() {
        List<Account> rootAccounts = accounts.stream()
                .filter(a -> a.getParentId() == null)
                .collect(Collectors.toList());
        
        for (Account root : rootAccounts) {
            buildAccountTree(root);
        }
        
        return rootAccounts;
    }

    private void buildAccountTree(Account parent) {
        List<Account> children = accounts.stream()
                .filter(a -> parent.getId().equals(a.getParentId()))
                .collect(Collectors.toList());
        
        for (Account child : children) {
            buildAccountTree(child);
        }
    }

    @Override
    public Account getAccountByCode(String code) {
        // TODO: Implement database access
        return null;
    }

    @Override
    public Account saveAccount(Account account) {
        // TODO: Implement database access
        return account;
    }

    @Override
    public List<Account> getRecentAccounts() {
        return new ArrayList<>(recentAccounts);
    }

    @Override
    public void addRecentAccount(Account account) {
        recentAccounts.remove(account);
        recentAccounts.add(0, account);
        if (recentAccounts.size() > MAX_RECENT_ACCOUNTS) {
            recentAccounts.remove(recentAccounts.size() - 1);
        }
    }
} 