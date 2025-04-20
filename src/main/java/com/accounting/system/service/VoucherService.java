package com.accounting.system.service;

import com.accounting.system.model.Voucher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    void updateVoucher(Voucher voucher);
    void deleteVoucher(Long id);
    Optional<Voucher> getVoucherById(Long id);
    List<Voucher> getAllVouchers();
    List<Voucher> searchVouchers(String keyword, LocalDate startDate, LocalDate endDate);
    void verifyVoucher(Long id, Long verifiedBy);
    String generateVoucherNumber();
    boolean isBalanced(Voucher voucher);
    Voucher getVoucherById(Long id);
    Voucher saveVoucher(Voucher voucher);
    List<Voucher> searchVouchers(String keyword);
} 