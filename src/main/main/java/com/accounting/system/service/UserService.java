package com.accounting.system.service;

import com.accounting.system.model.User;

/**
 * Service interface for user authentication and session management.
 * Provides methods for user authentication, credential management, and session handling.
 */
public interface UserService {
    /**
     * Authenticates a user with the provided credentials.
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return The authenticated User object if successful, null otherwise
     */
    User authenticate(String username, String password);

    /**
     * Saves the username for future automatic login.
     * @param username The username to save
     */
    void saveUsername(String username);

    /**
     * Retrieves the saved username for automatic login.
     * @return The saved username, or empty string if none exists
     */
    String getSavedUsername();

    /**
     * Clears the saved username.
     */
    void clearSavedUsername();

    /**
     * Starts a new user session.
     * @param username The username of the user starting the session
     */
    void startSession(String username);

    /**
     * Ends the current user session.
     */
    void endSession();

    /**
     * Checks if a user is currently logged in.
     * @return true if a user is logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Gets the username of the currently logged in user.
     * @return The current user's username, or null if no user is logged in
     */
    String getCurrentUser();

    /**
     * Gets the session token for the current session.
     * @return The session token, or null if no session is active
     */
    String getSessionToken();
} 