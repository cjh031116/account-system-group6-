package com.accounting.system.service;

import com.accounting.system.model.Voucher;

import java.util.List;

public interface AiService {

    String query(String message);
    String category(String desc);
    String suggestion(List<Voucher> voucherList);

}
