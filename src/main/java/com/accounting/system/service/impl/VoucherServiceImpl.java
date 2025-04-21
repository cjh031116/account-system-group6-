package com.accounting.system.service.impl;

import com.accounting.system.model.Voucher;
import com.accounting.system.model.VoucherEntry;
import com.accounting.system.service.VoucherService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class VoucherServiceImpl implements VoucherService {
    private final List<Voucher> vouchers;
    private final AtomicLong sequence;

    public VoucherServiceImpl() {
        this.vouchers = new ArrayList<>();
        this.sequence = new AtomicLong(1);
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        voucher.setId(sequence.getAndIncrement());
        voucher.setVoucherNo(generateVoucherNumber());
        voucher.setCreatedAt(LocalDateTime.now());
        voucher.setStatus("DRAFT");
        vouchers.add(voucher);
        return voucher;
    }

    @Override
    public void updateVoucher(Voucher voucher) {
        vouchers.stream()
                .filter(v -> v.getId().equals(voucher.getId()))
                .findFirst()
                .ifPresent(v -> {
                    v.setVoucherNo(voucher.getVoucherNo());
                    v.setDate(voucher.getDate());
                    v.setDescription(voucher.getDescription());
                    v.setEntries(voucher.getEntries());
                    v.setStatus(voucher.getStatus());
                    v.setLastModifiedDate(LocalDateTime.now().toLocalDate());
                });
    }

    @Override
    public void deleteVoucher(Long id) {
        vouchers.removeIf(v -> v.getId().equals(id));
    }

    @Override
    public Optional<Voucher> getVoucherById(Long id) {
        return vouchers.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return new ArrayList<>(vouchers);
    }

    @Override
    public List<Voucher> searchVouchers(String keyword, LocalDate startDate, LocalDate endDate) {
        return vouchers.stream()
                .filter(v -> {
                    boolean matchesKeyword = keyword == null || 
                            v.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                            v.getVoucherNo().toLowerCase().contains(keyword.toLowerCase());
                    boolean matchesDate = (startDate == null || !v.getDate().isBefore(startDate)) &&
                            (endDate == null || !v.getDate().isAfter(endDate));
                    return matchesKeyword && matchesDate;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void verifyVoucher(Long id, Long verifiedBy) {
        vouchers.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .ifPresent(v -> {
                    v.setStatus("VERIFIED");
                    v.setVerifiedBy(verifiedBy);
                    v.setVerifiedAt(LocalDateTime.now());
                });
    }

    @Override
    public String generateVoucherNumber() {
        return "V-" + String.format("%06d", sequence.get());
    }

    @Override
    public boolean isBalanced(Voucher voucher) {
        double debitTotal = voucher.getEntries().stream()
                .filter(e -> e.getDebit() > 0)
                .mapToDouble(VoucherEntry::getDebit)
                .sum();
        double creditTotal = voucher.getEntries().stream()
                .filter(e -> e.getCredit() > 0)
                .mapToDouble(VoucherEntry::getCredit)
                .sum();
        return Math.abs(debitTotal - creditTotal) < 0.01;
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        if (voucher.getId() == null) {
            return createVoucher(voucher);
        } else {
            updateVoucher(voucher);
            return voucher;
        }
    }

    @Override
    public List<Voucher> searchVouchers(String keyword) {
        return searchVouchers(keyword, null, null);
    }
} 