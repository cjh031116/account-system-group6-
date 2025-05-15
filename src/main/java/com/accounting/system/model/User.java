package com.accounting.system.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

/**
 * Represents a user in the accounting system.
 * Contains user authentication and profile information.
 */
public class User {
    // User identification
    private final SimpleLongProperty id; // Unique identifier for the user
    private final SimpleStringProperty username; // User's login name
    private final SimpleStringProperty password; // Hashed password
    private final SimpleStringProperty email; // User's email address
    private final SimpleStringProperty role; // User's role (e.g., ADMIN, USER)
    
    // User status and preferences
    private boolean active; // Whether the user account is active
    private LocalDateTime lastLogin; // Timestamp of last successful login
    private boolean rememberMe; // Whether to remember user credentials

    /**
     * Default constructor initializes all properties with default values.
     */
    public User() {
        this.id = new SimpleLongProperty();
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
    }

    /**
     * Constructor with parameters to initialize a user with basic credentials.
     * @param username User's login name
     * @param password User's password (will be hashed)
     */
    public User(String username, String password) {
        this();
        setUsername(username);
        setPassword(password);
        setRole("USER");
    }

    // Getters and Setters with property support for JavaFX binding
    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
} 