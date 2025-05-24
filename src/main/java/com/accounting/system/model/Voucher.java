package com.accounting.system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a voucher in the accounting system.
 * A voucher is a document that records financial transactions with debit and credit entries.
 */
public class Voucher {
    // Basic voucher information
    private String id; // Unique identifier for the voucher
    private String voucherNo; // Voucher number (e.g., "V-2023-001")
    private LocalDate date; // Date when the voucher was created
    private String description; // Description of the voucher's purpose
    private String category;
    private String direction;
    private String month;
    
    // Transaction details
    private List<VoucherEntry> entries; // List of debit and credit entries
    private double totalDebit; // Total amount of all debit entries
    private double totalCredit; // Total amount of all credit entries
    
    // Status and tracking information
    private String status; // Current status: DRAFT, POSTED, VERIFIED
    private String createdBy; // User who created the voucher
    private LocalDate createdDate; // Date when the voucher was created
    private String lastModifiedBy; // User who last modified the voucher
    private LocalDate lastModifiedDate; // Date of last modification
    private LocalDateTime createdAt; // Timestamp of creation
    private LocalDate verifiedDate; // Date when the voucher was verified
    private Long verifiedBy; // User ID who verified the voucher

    /**
     * Default constructor initializes a new voucher with default values.
     * Sets status to DRAFT, current date, and empty entries list.
     */
    public Voucher() {
        this.entries = new ArrayList<>();
        this.status = "DRAFT";
        this.date = LocalDate.now();
        this.month = this.date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters with documentation
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<VoucherEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<VoucherEntry> entries) {
        this.entries = entries;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    @JsonIgnore
    public double getDebitAmount() {
        return totalDebit;
    }

    @JsonIgnore
    public double getCreditAmount() {
        return totalCredit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public Long getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Long verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    /**
     * Adds a new entry to the voucher and recalculates totals.
     * @param entry The voucher entry to add
     */
    public void addEntry(VoucherEntry entry) {
        this.entries.add(entry);
        recalculateTotals();
    }

    /**
     * Removes an entry from the voucher and recalculates totals.
     * @param entry The voucher entry to remove
     */
    public void removeEntry(VoucherEntry entry) {
        this.entries.remove(entry);
        recalculateTotals();
    }

    /**
     * Recalculates the total debit and credit amounts based on all entries.
     */
    private void recalculateTotals() {
        this.totalDebit = 0;
        this.totalCredit = 0;
        for (VoucherEntry entry : entries) {
            this.totalDebit += entry.getDebit();
            this.totalCredit += entry.getCredit();
        }
    }
} 