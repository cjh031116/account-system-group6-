package com.accounting.system.service;

import com.accounting.system.model.Account;
import com.accounting.system.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountServiceTest {

    @Test
    public void testGetAllAccounts() {
        AccountService accountService = new AccountServiceImpl();
        List<Account> accountList = accountService.getAllAccounts();
        System.out.println(accountList);
        assertEquals(6, accountList.size(), "account count should be 6");
    }

    @Test
    public void testGetActiveAccounts() {
        AccountService accountService = new AccountServiceImpl();
        List<Account> accountList = accountService.getActiveAccounts();
        System.out.println(accountList);
        assertEquals(6, accountList.size(), "active account count should be 6");
    }

    @Test
    public void testGetAccountByCode() {
        AccountService accountService = new AccountServiceImpl();
        Account account = accountService.getAccountByCode("1001");
        System.out.println(account);
        assertEquals("Cash", account.getName(), "account name is wrong");
    }

}
