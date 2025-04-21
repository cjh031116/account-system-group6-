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

public class VoucherServiceImpl implements VoucherService {
    private final List<Voucher> vouchers = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Voucher createVoucher(Voucher voucher) {
        voucher.setId(sequence.getAndIncrement());
        voucher.setVoucherNo(generateVoucherNumber());
        vouchers.add(voucher);
        return voucher;
    }

    @Override
    public void updateVoucher(Voucher voucher) {
        for (int i = 0; i < vouchers.size(); i++) {
            if (vouchers.get(i).getId().equals(voucher.getId())) {
                vouchers.set(i, voucher);
                break;
            }
        }
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
                .filter(v -> (keyword == null || 
                            v.getVoucherNo().contains(keyword) || 
                            v.getDescription().contains(keyword)) &&
                           (startDate == null || !v.getDate().isBefore(startDate)) &&
                           (endDate == null || !v.getDate().isAfter(endDate)))
                .toList();
    }

    @Override
    public void verifyVoucher(Long id, Long verifiedBy) {
        getVoucherById(id).ifPresent(voucher -> {
            voucher.setStatus("VERIFIED");
            voucher.setVerifiedBy(verifiedBy);
            voucher.setVerifiedAt(LocalDateTime.now());
            updateVoucher(voucher);
        });
    }

    @Override
    public String generateVoucherNumber() {
        return String.format("V%06d", sequence.get());
    }

    @Override
    public boolean isBalanced(Voucher voucher) {
        double totalDebit = 0;
        double totalCredit = 0;
        
        for (VoucherEntry entry : voucher.getEntries()) {
            totalDebit += entry.getDebit();
            totalCredit += entry.getCredit();
        }
        
        return Math.abs(totalDebit - totalCredit) < 0.01;
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        // TODO: Implement database access
        return voucher;
    }

    @Override
    public List<Voucher> searchVouchers(String keyword) {
        // TODO: Implement search functionality
        return new ArrayList<>();
    }
} 