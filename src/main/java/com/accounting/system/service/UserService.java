package com.accounting.system.service;

public interface UserService {
    boolean authenticate(String username, String password);
    void saveUsername(String username);
    String getSavedUsername();
    void clearSavedUsername();
    void startSession(String username);
    void endSession();
    boolean isLoggedIn();
    String getCurrentUser();
} 