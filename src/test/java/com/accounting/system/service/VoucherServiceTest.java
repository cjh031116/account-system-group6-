package com.accounting.system.service;

import com.accounting.system.model.Voucher;
import com.accounting.system.service.impl.VoucherServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
        // Assuming there is at least one voucher in the database
        assertFalse(voucherService.getAllVouchers().isEmpty(), "voucher list should not be empty");

        // Get the first voucher to check if it is balanced
        if (voucherService.getAllVouchers().isEmpty()) {
            throw new IllegalStateException("No vouchers available for testing");
        }
        List<Voucher> list = voucherService.getAllVouchers();
        System.out.println(list.get(0));

        // Check if the first voucher is balanced
        // Assuming the first voucher is a valid one for testing
        if (list.get(0) == null) {
            throw new IllegalStateException("The first voucher is null");
        }
        boolean isBalanced = voucherService.isBalanced(list.get(0));
        assertTrue(isBalanced, "voucher should be balanced");
    }
    @Test
    public void testGetVoucherById() {
        VoucherService voucherService = new VoucherServiceImpl();
        List<Voucher> list = voucherService.getAllVouchers();
        assertFalse(list.isEmpty(), "voucher list should not be empty");

        // Assuming the first voucher exists
        if (list.isEmpty()) {
            throw new IllegalStateException("No vouchers available for testing");
        }
        Optional<Voucher> voucher = voucherService.getVoucherById(list.get(0).getId());
        assertTrue(voucher != null, "voucher should not be null");
    }
    @Test
    public void testSearchVouchers() {
        VoucherService voucherService = new VoucherServiceImpl();
        List<Voucher> list = voucherService.searchVouchers("test", null, null);
        assertFalse(list.isEmpty(), "search vouchers should not be empty");
    }



    @Test
    public void testUpdateVoucher() {
        VoucherService voucherService = new VoucherServiceImpl();
        List<Voucher> list = voucherService.getAllVouchers();
        assertFalse(list.isEmpty(), "voucher list should not be empty");

        // Assuming the first voucher exists
        if (list.isEmpty()) {
            throw new IllegalStateException("No vouchers available for testing");
        }
        Voucher voucherToUpdate = list.get(0);
        voucherToUpdate.setDescription("Updated Description");
        voucherService.updateVoucher(voucherToUpdate);
        Optional<Voucher> updatedVoucher = voucherService.getVoucherById(voucherToUpdate.getId());
        assertTrue(updatedVoucher.isPresent(), "updated voucher should be present");
    }
}
