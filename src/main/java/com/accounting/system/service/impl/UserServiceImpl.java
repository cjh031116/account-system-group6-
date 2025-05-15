package com.accounting.system.service.impl;

import com.accounting.system.model.User;
import com.accounting.system.service.UserService;

import java.time.LocalDateTime;

/**
 * Implementation of the UserService interface.
 * Provides in-memory user authentication and session management.
 */
public class UserServiceImpl implements UserService {
    // Current user's username
    private String currentUser;
    
    // Session token for the current session
    private String sessionToken;
    
    // Timestamp when the session started
    private LocalDateTime sessionStartTime;
    
    /**
     * Constructor initializes the service with default values.
     */
    public UserServiceImpl() {
        this.sessionToken = null;
    }
    
    /**
     * Authenticates a user with the provided credentials.
     * Currently accepts any non-empty credentials for demonstration purposes.
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return The authenticated User object if successful, null otherwise
     */
    @Override
    public User authenticate(String username, String password) {
        // TODO: Implement actual authentication against database
        // For now, accept any non-empty credentials
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setActive(true);
            user.setRole("USER");
            currentUser = username;
            startSession(username);
            return user;
        }
        return null;
    }
    
    /**
     * Saves the username for future automatic login.
     * Currently a placeholder for future implementation.
     * @param username The username to save
     */
    @Override
    public void saveUsername(String username) {
        // TODO: Implement persistent storage
    }
    
    /**
     * Retrieves the saved username for automatic login.
     * Currently returns an empty string as persistent storage is not implemented.
     * @return The saved username, or empty string if none exists
     */
    @Override
    public String getSavedUsername() {
        // TODO: Implement persistent storage
        return "";
    }
    
    /**
     * Clears the saved username.
     * Currently a placeholder for future implementation.
     */
    @Override
    public void clearSavedUsername() {
        // TODO: Implement persistent storage
    }
    
    /**
     * Starts a new user session.
     * Initializes session properties with current timestamp and generates a session token.
     * @param username The username of the user starting the session
     */
    @Override
    public void startSession(String username) {
        currentUser = username;
        sessionStartTime = LocalDateTime.now();
        sessionToken = generateSessionToken();
    }
    
    /**
     * Ends the current user session.
     * Clears all session-related properties.
     */
    @Override
    public void endSession() {
        currentUser = null;
        sessionToken = null;
        sessionStartTime = null;
    }
    
    /**
     * Checks if a user is currently logged in.
     * Verifies that the session is active and not expired (24-hour limit).
     * @return true if a user is logged in and session is valid, false otherwise
     */
    @Override
    public boolean isLoggedIn() {
        return currentUser != null && sessionToken != null && 
               sessionStartTime != null && 
               sessionStartTime.plusHours(24).isAfter(LocalDateTime.now());
    }
    
    /**
     * Gets the username of the currently logged in user.
     * @return The current user's username, or null if no user is logged in
     */
    @Override
    public String getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Gets the session token for the current session.
     * @return The session token, or null if no session is active
     */
    @Override
    public String getSessionToken() {
        return sessionToken;
    }
    
    /**
     * Generates a unique session token using UUID.
     * @return A unique session token string
     */
    private String generateSessionToken() {
        return java.util.UUID.randomUUID().toString();
    }
} 