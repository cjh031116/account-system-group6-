package com.accounting.system.service;

import com.accounting.system.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(Long id);
    Optional<Account> getAccountById(Long id);
    List<Account> getAllAccounts();
    List<Account> getActiveAccounts();
    List<Account> searchAccounts(String keyword);

} 