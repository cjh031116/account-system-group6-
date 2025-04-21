package com.accounting.system.service.impl;

import com.accounting.system.service.UserService;
import java.time.LocalDateTime;

public class UserServiceImpl implements UserService {
    private String currentUser;
    private String sessionToken;
    private LocalDateTime sessionStartTime;
    
    public UserServiceImpl() {
        this.sessionToken = null;
    }
    
    @Override
    public boolean authenticate(String username, String password) {
        // TODO: Implement actual authentication against database
        // For now, accept any non-empty credentials
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            currentUser = username;
            startSession(username);
            return true;
        }
        return false;
    }
    
    @Override
    public void saveUsername(String username) {
        // TODO: Implement persistent storage
    }
    
    @Override
    public String getSavedUsername() {
        // TODO: Implement persistent storage
        return "";
    }
    
    @Override
    public void clearSavedUsername() {
        // TODO: Implement persistent storage
    }
    
    @Override
    public void startSession(String username) {
        currentUser = username;
        sessionStartTime = LocalDateTime.now();
        sessionToken = generateSessionToken();
    }
    
    @Override
    public void endSession() {
        currentUser = null;
        sessionToken = null;
        sessionStartTime = null;
    }
    
    @Override
    public boolean isLoggedIn() {
        return currentUser != null && sessionToken != null && 
               sessionStartTime != null && 
               sessionStartTime.plusHours(24).isAfter(LocalDateTime.now());
    }
    
    @Override
    public String getCurrentUser() {
        return currentUser;
    }
    
    @Override
    public String getSessionToken() {
        return sessionToken;
    }
    
    private String generateSessionToken() {
        return java.util.UUID.randomUUID().toString();
    }
} 