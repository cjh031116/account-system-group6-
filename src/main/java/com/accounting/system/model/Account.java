package com.accounting.system.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Account {
    private final SimpleLongProperty id;
    private final SimpleStringProperty code;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleBooleanProperty active;
    private final SimpleLongProperty parentId;
    private final SimpleDoubleProperty balance;

    public Account() {
        this.id = new SimpleLongProperty();
        this.code = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.active = new SimpleBooleanProperty(true);
        this.parentId = new SimpleLongProperty();
        this.balance = new SimpleDoubleProperty(0);
    }

    public Account(Long id, String code, String name, String type, boolean active, Long parentId) {
        this();
        setId(id);
        setCode(code);
        setName(name);
        setType(type);
        setActive(active);
        setParentId(parentId);
    }

    // Getters and Setters
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

    public void setParentId(Long parentId) {
        if (parentId != null) {
            this.parentId.set(parentId);
        } else {
            this.parentId.set(0L); // 使用0表示没有父账户
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

    @Override
    public String toString() {
        return getCode() + " - " + getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.get() == account.id.get();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id.get());
    }
} 