package com.accounting.system.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private Long id;
    private String accountCode;
    private String accountName;
    private String accountType; // ASSET, LIABILITY, EQUITY, INCOME, EXPENSE
    private String parentAccountCode;
    private Boolean isGroup;
    private BigDecimal openingBalance;
    private BigDecimal currentBalance;
    private String description;
    private Boolean isActive;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    private Integer level;
    private String currency;
    private Boolean isSystemAccount;
    private String tags;
    private String notes;

    public Account() {
        this.openingBalance = BigDecimal.ZERO;
        this.currentBalance = BigDecimal.ZERO;
        this.isGroup = false;
        this.isActive = true;
        this.isSystemAccount = false;
        this.level = 1;
        this.currency = "CNY";
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getParentAccountCode() {
        return parentAccountCode;
    }

    public void setParentAccountCode(String parentAccountCode) {
        this.parentAccountCode = parentAccountCode;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance != null ? openingBalance : BigDecimal.ZERO;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance != null ? currentBalance : BigDecimal.ZERO;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsSystemAccount() {
        return isSystemAccount;
    }

    public void setIsSystemAccount(Boolean isSystemAccount) {
        this.isSystemAccount = isSystemAccount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void updateBalance(BigDecimal amount) {
        this.currentBalance = this.currentBalance.add(amount);
    }

    public boolean isDebitAccount() {
        return "ASSET".equals(this.accountType) || "EXPENSE".equals(this.accountType);
    }

    public boolean isCreditAccount() {
        return "LIABILITY".equals(this.accountType) || "EQUITY".equals(this.accountType) 
            || "INCOME".equals(this.accountType);
    }

    public BigDecimal getBalance() {
        return isDebitAccount() ? currentBalance : currentBalance.negate();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", accountCode, accountName);
    }
} 