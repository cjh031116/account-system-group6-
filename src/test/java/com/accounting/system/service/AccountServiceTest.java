package com.accounting.system.service;

import com.accounting.system.model.Account;
import com.accounting.system.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountServiceTest {



    @Test
    public void testUpdateAccount() {
        AccountService accountService = new AccountServiceImpl();
        Account account = accountService.getAccountByCode("1001");
        if (account != null) {
            account.setName("Updated Cash");
            Account updatedAccount = accountService.updateAccount(account);
            System.out.println(updatedAccount);
            assertEquals("Updated Cash", updatedAccount.getName(), "account name should be updated");
        }
    }

    @Test
    public void testDeleteAccount() {
        AccountService accountService = new AccountServiceImpl();
        Long idToDelete = 1L; // Assuming an account with ID 1 exists
        accountService.deleteAccount(idToDelete);
    }

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
