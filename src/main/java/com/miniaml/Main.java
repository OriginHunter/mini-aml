package com.miniaml;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;

public class Main {
    public static void main(String[] args) {
        Transaction t = new Transaction(
                10001L,
                1L,
                new BigDecimal("60000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10,30));
        System.out.println(t.getId());
        System.out.println(t.getAccountId());
        System.out.println(t.getAmount());
        System.out.println(t.getType());
        System.out.println(t.getTransTime());
        System.out.println(t);
        t.setId(10002L);
        t.setAccountId(2L);
        t.setAmount(new BigDecimal("30000.00"));
        t.setType("OUT");
        t.setTransTime(LocalDateTime.of(2026, 9, 14, 9, 50));
        System.out.println(t);

    }
}
