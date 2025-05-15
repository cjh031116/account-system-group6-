package com.accounting.system.service.impl;

import com.accounting.system.model.Voucher;
import com.accounting.system.service.VoucherService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementation of the VoucherService interface.
 * Provides file-based storage and management of vouchers using JSON serialization.
 */
public class VoucherServiceImpl implements VoucherService {
    // File storage configuration
    private static final String DATA_FILE = "vouchers.txt"; // File path for storing vouchers
    
    // Service dependencies
    private final ObjectMapper objectMapper; // JSON serializer/deserializer
    private List<Voucher> vouchers; // In-memory cache of vouchers
    private final AtomicLong sequence; // Sequence generator for voucher numbers

    /**
     * Constructor initializes the service and loads existing vouchers.
     */
    public VoucherServiceImpl() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.vouchers = new ArrayList<>();
        this.sequence = new AtomicLong(1);
        loadVouchers();
    }

    /**
     * Loads vouchers from the data file into memory.
     * Creates an empty list if the file doesn't exist or there's an error.
     */
    private void loadVouchers() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                vouchers = objectMapper.readValue(file, new TypeReference<List<Voucher>>() {});
            } catch (IOException e) {
                System.err.println("Error loading vouchers: " + e.getMessage());
                vouchers = new ArrayList<>();
            }
        }
    }

    /**
     * Saves the current list of vouchers to the data file.
     */
    private void saveVouchers() {
        try {
            objectMapper.writeValue(new File(DATA_FILE), vouchers);
        } catch (IOException e) {
            System.err.println("Error saving vouchers: " + e.getMessage());
        }
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        if (voucher.getId() == null) {
            voucher.setId(UUID.randomUUID().toString());
        }
        voucher.setVoucherNo(generateVoucherNumber());
        voucher.setCreatedDate(LocalDate.now());
        voucher.setStatus("DRAFT");
        vouchers.add(voucher);
        saveVouchers();
        return voucher;
    }

    @Override
    public void updateVoucher(Voucher voucher) {
        for (int i = 0; i < vouchers.size(); i++) {
            if (vouchers.get(i).getId().equals(voucher.getId())) {
                vouchers.set(i, voucher);
                saveVouchers();
                return;
            }
        }
        throw new IllegalArgumentException("Voucher not found with id: " + voucher.getId());
    }

    @Override
    public void deleteVoucher(String id) {
        if (vouchers.removeIf(voucher -> voucher.getId().equals(id))) {
            saveVouchers();
        } else {
            throw new IllegalArgumentException("Voucher not found with id: " + id);
        }
    }

    @Override
    public Optional<Voucher> getVoucherById(String id) {
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
                    boolean matches = true;
                    if (keyword != null && !keyword.isEmpty()) {
                        matches = v.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                                v.getVoucherNo().toLowerCase().contains(keyword.toLowerCase());
                    }
                    if (startDate != null) {
                        matches = matches && !v.getDate().isBefore(startDate);
                    }
                    if (endDate != null) {
                        matches = matches && !v.getDate().isAfter(endDate);
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void verifyVoucher(String id, Long userId) {
        Voucher voucher = getVoucherById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found with id: " + id));
        if (!isBalanced(voucher)) {
            throw new IllegalStateException("Cannot verify unbalanced voucher");
        }
        voucher.setStatus("VERIFIED");
        voucher.setVerifiedBy(userId);
        voucher.setVerifiedDate(LocalDate.now());
        saveVoucher(voucher);
    }

    @Override
    public String generateVoucherNumber() {
        String prefix = "V";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = (int) vouchers.stream()
                .filter(v -> v.getVoucherNo().startsWith(prefix + date))
                .count();
        return String.format("%s%s%04d", prefix, date, count + 1);
    }

    @Override
    public boolean isBalanced(Voucher voucher) {
        double totalDebit = voucher.getEntries().stream()
                .mapToDouble(e -> e.getDebit())
                .sum();
        double totalCredit = voucher.getEntries().stream()
                .mapToDouble(e -> e.getCredit())
                .sum();
        return Math.abs(totalDebit - totalCredit) < 0.01;
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        try {
            if (voucher.getId() == null) {
                // Create new voucher
                voucher.setId(UUID.randomUUID().toString());
                voucher.setVoucherNo(generateVoucherNumber());
                voucher.setCreatedDate(LocalDate.now());
                voucher.setStatus("DRAFT");
                vouchers.add(voucher);
            } else {
                // Update existing voucher
                for (int i = 0; i < vouchers.size(); i++) {
                    if (vouchers.get(i).getId().equals(voucher.getId())) {
                        vouchers.set(i, voucher);
                        break;
                    }
                }
            }
            saveVouchers();
            return voucher;
        } catch (Exception e) {
            System.err.println("Error saving voucher: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save voucher", e);
        }
    }

    @Override
    public List<Voucher> searchVouchers(String keyword) {
        return searchVouchers(keyword, null, null);
    }

    @Override
    public void exportVouchers(List<Voucher> vouchers, String filename) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            writer.write("Voucher No,Date,Description,Total Debit,Total Credit,Status\n");
            
            // Write data
            for (Voucher voucher : vouchers) {
                writer.write(String.format("%s,%s,%s,%.2f,%.2f,%s\n",
                    voucher.getVoucherNo(),
                    voucher.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    voucher.getDescription(),
                    voucher.getTotalDebit(),
                    voucher.getTotalCredit(),
                    voucher.getStatus()
                ));
            }
        }
    }
} 