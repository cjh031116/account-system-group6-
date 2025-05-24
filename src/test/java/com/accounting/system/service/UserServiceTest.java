package com.accounting.system.service;

import com.accounting.system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserServiceTest {

    @Test
    public void testSaveUsername() {
        UserService userService = new UserServiceImpl();
        userService.saveUsername("username");
    }

    @Test
    public void testGetCurrentUser() {
        UserService userService = new UserServiceImpl();
        userService.authenticate("admin", "123456");
        String username = userService.getCurrentUser();
        System.out.println(username);
        assertEquals("admin", username, "current user is wrong");
    }

    @Test
    public void test() {
        UserService userService = new UserServiceImpl();
        assertFalse(userService.isLoggedIn(), "should not be logged in");
    }

}
