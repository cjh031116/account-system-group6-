package com.accounting.system.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * Represents a single entry in a voucher.
 * Each entry records a debit or credit transaction for a specific account.
 */
public class VoucherEntry {
    // Identification properties
    private final SimpleLongProperty id; // Unique identifier for the entry
    private final SimpleLongProperty voucherId; // ID of the parent voucher
    private final SimpleStringProperty voucherNo; // Voucher number for reference
    
    // Transaction details
    private final SimpleObjectProperty<Account> account; // Account affected by this entry
    private final SimpleDoubleProperty debit; // Debit amount (positive)
    private final SimpleDoubleProperty credit; // Credit amount (positive)
    private final SimpleStringProperty description; // Description of the transaction
    
    // Date information
    private final SimpleObjectProperty<LocalDate> date; // Date of the transaction

    /**
     * Default constructor initializes all properties with default values.
     */
    public VoucherEntry() {
        this.id = new SimpleLongProperty();
        this.voucherId = new SimpleLongProperty();
        this.account = new SimpleObjectProperty<>();
        this.debit = new SimpleDoubleProperty(0);
        this.credit = new SimpleDoubleProperty(0);
        this.description = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>();
        this.voucherNo = new SimpleStringProperty();
    }

    /**
     * Constructor with parameters to initialize a voucher entry with specific values.
     * @param id Unique identifier for the entry
     * @param voucherId ID of the parent voucher
     * @param account Account affected by this entry
     * @param debit Debit amount
     * @param credit Credit amount
     * @param description Description of the transaction
     */
    public VoucherEntry(Long id, Long voucherId, Account account, double debit, double credit, String description) {
        this();
        setId(id);
        setVoucherId(voucherId);
        setAccount(account);
        setDebit(debit);
        setCredit(credit);
        setDescription(description);
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

    public Long getVoucherId() {
        return voucherId.get();
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId.set(voucherId);
    }

    public SimpleLongProperty voucherIdProperty() {
        return voucherId;
    }

    public Account getAccount() {
        return account.get();
    }

    public void setAccount(Account account) {
        this.account.set(account);
    }

    public SimpleObjectProperty<Account> accountProperty() {
        return account;
    }

    public double getDebit() {
        return debit.get();
    }

    public void setDebit(double debit) {
        this.debit.set(debit);
    }

    public SimpleDoubleProperty debitProperty() {
        return debit;
    }

    public double getCredit() {
        return credit.get();
    }

    public void setCredit(double credit) {
        this.credit.set(credit);
    }

    public SimpleDoubleProperty creditProperty() {
        return credit;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public String getVoucherNo() {
        return voucherNo.get();
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo.set(voucherNo);
    }

    public SimpleStringProperty voucherNoProperty() {
        return voucherNo;
    }

    /**
     * Returns a string representation of the voucher entry.
     * Format: "Account - Debit: X.XX, Credit: Y.YY"
     * @return Formatted string representation of the entry
     */
    @Override
    public String toString() {
        return String.format("%s - Debit: %.2f, Credit: %.2f", 
            getAccount() != null ? getAccount().toString() : "No Account",
            getDebit(), getCredit());
    }

    /**
     * Compares this voucher entry with another object for equality based on ID.
     * @param o Object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoucherEntry that = (VoucherEntry) o;
        return id.get() == that.id.get();
    }

    /**
     * Returns a hash code value for this voucher entry based on its ID.
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id.get());
    }
} 