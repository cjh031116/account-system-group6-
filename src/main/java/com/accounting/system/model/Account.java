package com.accounting.system.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents an account in the accounting system.
 * An account is a basic unit for recording financial transactions.
 */
/**
 * Represents an accounting account with properties for identification, status and balance.
 */
public class Account {
    // Account identification
    private final SimpleLongProperty id; // Unique identifier for the account
    private final SimpleStringProperty code; // Account code (e.g., "1001" for Cash)
    private final SimpleStringProperty name; // Account name (e.g., "Cash on Hand")
    private final SimpleStringProperty type; // Account type (e.g., "Asset", "Liability")
    
    // Account status and balance
    private final SimpleBooleanProperty active; // Whether the account is active
    private final SimpleLongProperty parentId; // ID of the parent account (for hierarchical structure)
    private final SimpleDoubleProperty balance; // Current balance of the account
    private String description; // Additional information about the account

    /**
     * Default constructor initializes all properties with default values.
     */
    public Account() {
        this.id = new SimpleLongProperty();
        this.code = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.active = new SimpleBooleanProperty(true);
        this.parentId = new SimpleLongProperty();
        this.balance = new SimpleDoubleProperty(0);
    }

    /**
     * Constructor with parameters to initialize an account with specific values.
     * @param id Unique identifier for the account
     * @param code Account code
     * @param name Account name
     * @param type Account type
     * @param active Whether the account is active
     * @param parentId ID of the parent account
     */
    public Account(Long id, String code, String name, String type, boolean active, Long parentId) {
        this();
        setId(id);
        setCode(code);
        setName(name);
        setType(type);
        setActive(active);
        setParentId(parentId);
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

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public SimpleBooleanProperty activeProperty() {
        return active;
    }

    public Long getParentId() {
        return parentId.get();
    }

    /**
     * Sets the parent account ID. If null is provided, sets to 0 indicating no parent.
     * @param parentId ID of the parent account
     */
    public void setParentId(Long parentId) {
        if (parentId != null) {
            this.parentId.set(parentId);
        } else {
            this.parentId.set(0L); // Use 0 to indicate no parent account
        }
    }

    public SimpleLongProperty parentIdProperty() {
        return parentId;
    }

    public double getBalance() {
        return balance.get();
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

    public SimpleDoubleProperty balanceProperty() {
        return balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a string representation of the account in the format "code - name".
     * @return Formatted string representation of the account
     */
    @Override
    public String toString() {
        return getCode() + " - " + getName();
    }
}