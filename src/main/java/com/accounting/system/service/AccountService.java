package com.accounting.system.service;

import com.accounting.system.model.Account;
import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    List<Account> getActiveAccounts();
    Account getAccountById(Long id);
    Account getAccountByCode(String code);
    Account createAccount(Account account);
    Account updateAccount(Account account);
    void deleteAccount(Long id);
    List<Account> searchAccounts(String keyword);
    List<Account> getChildAccounts(Long parentId);
    boolean isAccountCodeUnique(String code, Long excludeId);
} 