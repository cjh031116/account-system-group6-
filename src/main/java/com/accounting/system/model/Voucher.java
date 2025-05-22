package com.accounting.system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Voucher {
    private Long id;
    private String voucherNo;
    private LocalDate date;
    private String description;
    private double totalDebit;
    private double totalCredit;
    private String status; // DRAFT, POSTED, VERIFIED
    private String createdBy;
    private LocalDate createdDate;
    private String lastModifiedBy;
    private LocalDate lastModifiedDate;
    private LocalDateTime createdAt;
    private Long verifiedBy;
    private LocalDateTime verifiedAt;
    private List<VoucherEntry> entries;

    public Voucher() {
        this.entries = new ArrayList<>();
        this.status = "DRAFT";
        this.date = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Long verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public List<VoucherEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<VoucherEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }

    public void addEntry(VoucherEntry entry) {
        this.entries.add(entry);
        recalculateTotals();
    }

    public void removeEntry(VoucherEntry entry) {
        this.entries.remove(entry);
        recalculateTotals();
    }

    private void recalculateTotals() {
        this.totalDebit = 0;
        this.totalCredit = 0;
        for (VoucherEntry entry : entries) {
            this.totalDebit += entry.getDebit();
            this.totalCredit += entry.getCredit();
        }
    }
} 