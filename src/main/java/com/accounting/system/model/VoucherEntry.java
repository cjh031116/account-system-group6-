package com.accounting.system.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class VoucherEntry {
    private final SimpleLongProperty id;
    private final SimpleLongProperty voucherId;
    private final SimpleObjectProperty<Account> account;
    private final SimpleDoubleProperty debit;
    private final SimpleDoubleProperty credit;
    private final SimpleStringProperty description;
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleStringProperty voucherNo;

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

    public VoucherEntry(Long id, Long voucherId, Account account, double debit, double credit, String description) {
        this();
        setId(id);
        setVoucherId(voucherId);
        setAccount(account);
        setDebit(debit);
        setCredit(credit);
        setDescription(description);
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

    @Override
    public String toString() {
        return String.format("%s - Debit: %.2f, Credit: %.2f", 
            getAccount() != null ? getAccount().toString() : "No Account",
            getDebit(), getCredit());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoucherEntry that = (VoucherEntry) o;
        return id.get() == that.id.get();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id.get());
    }
} 