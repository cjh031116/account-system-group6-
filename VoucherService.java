package com.accounting.system.service;

import com.accounting.system.model.Voucher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for voucher management.
 * Provides methods for CRUD operations and voucher-related queries.
 */
public interface VoucherService {
    /**
     * Creates a new voucher in the system.
     * @param voucher The voucher to create
     * @return The created voucher with generated ID and voucher number
     */
    Voucher createVoucher(Voucher voucher);

    /**
     * Updates an existing voucher in the system.
     * @param voucher The voucher to update
     * @throws IllegalArgumentException if voucher not found
     */
    void updateVoucher(Voucher voucher);

    /**
     * Deletes a voucher from the system.
     * @param id The ID of the voucher to delete
     * @throws IllegalArgumentException if voucher not found
     */
    void deleteVoucher(String id);

    /**
     * Retrieves a voucher by its unique identifier.
     * @param id The voucher ID to search for
     * @return Optional containing the voucher if found, empty otherwise
     */
    Optional<Voucher> getVoucherById(String id);

    /**
     * Retrieves all vouchers from the system.
     * @return List of all vouchers
     */
    List<Voucher> getAllVouchers();

    /**
     * Searches for vouchers matching the given criteria.
     * @param keyword Search term for voucher number or description
     * @param startDate Start date of the search period
     * @param endDate End date of the search period
     * @return List of matching vouchers
     */
    List<Voucher> searchVouchers(String keyword, LocalDate startDate, LocalDate endDate);

    /**
     * Verifies a voucher, marking it as verified.
     * @param id The ID of the voucher to verify
     * @param userId The ID of the user performing the verification
     * @throws IllegalArgumentException if voucher not found
     * @throws IllegalStateException if voucher is not balanced
     */
    void verifyVoucher(String id, Long userId);

    /**
     * Generates a new voucher number.
     * Format: V + yyyyMMdd + sequential number
     * @return The generated voucher number
     */
    String generateVoucherNumber();

    /**
     * Checks if a voucher is balanced (total debit equals total credit).
     * @param voucher The voucher to check
     * @return true if the voucher is balanced, false otherwise
     */
    boolean isBalanced(Voucher voucher);

    /**
     * Saves a voucher to the system.
     * Creates a new voucher if ID is null, updates existing otherwise.
     * @param voucher The voucher to save
     * @return The saved voucher
     * @throws RuntimeException if save operation fails
     */
    Voucher saveVoucher(Voucher voucher);

    /**
     * Searches for vouchers matching the given keyword.
     * Searches in voucher number and description.
     * @param keyword The search term
     * @return List of matching vouchers
     */
    List<Voucher> searchVouchers(String keyword);

    /**
     * Exports vouchers to a file.
     * @param vouchers The list of vouchers to export
     * @param filename The name of the file to export to
     * @throws Exception if export fails
     */
    void exportVouchers(List<Voucher> vouchers, String filename) throws Exception;
}