package com.accounting.system.service;

import com.accounting.system.model.Voucher;
import com.accounting.system.service.impl.VoucherServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoucherServiceTest {

    @Test
    public void testGetAllVouchers() {
        VoucherService voucherService = new VoucherServiceImpl();
        List<Voucher> list = voucherService.getAllVouchers();
        assertFalse(list.isEmpty(), "vouchers should not be empty");
    }

    @Test
    public void testIsBalanced() {
        VoucherService voucherService = new VoucherServiceImpl();
        List<Voucher> list = voucherService.getAllVouchers();
        boolean isBalanced = voucherService.isBalanced(list.get(0));
        assertTrue(isBalanced, "voucher should be balanced");
    }

}
