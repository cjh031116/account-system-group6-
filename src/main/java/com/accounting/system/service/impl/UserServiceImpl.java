package com.accounting.system.service.impl;

import com.accounting.system.service.UserService;

import java.util.prefs.Preferences;

public class UserServiceImpl implements UserService {
    private static final String PREF_USERNAME = "remembered_username";
    private String currentUser;
    private final Preferences prefs;
    
    public UserServiceImpl() {
        this.prefs = Preferences.userNodeForPackage(UserServiceImpl.class);
    }

    @Override
    public boolean authenticate(String username, String password) {
        // TODO: Implement actual authentication against database
        // For now, accept any non-empty credentials
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            currentUser = username;
            return true;
        }
        return false;
    }

    @Override
    public void saveUsername(String username) {
        prefs.put(PREF_USERNAME, username);
    }

    @Override
    public String getSavedUsername() {
        return prefs.get(PREF_USERNAME, "");
    }

    @Override
    public void clearSavedUsername() {
        prefs.remove(PREF_USERNAME);
    }

    @Override
    public void startSession(String username) {
        currentUser = username;
    }

    @Override
    public void endSession() {
        currentUser = null;
    }

    @Override
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }
} 