package com.miniaml;

import com.miniaml.model.Transaction;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Transaction t1 = new Transaction(10001L, 1L,
                new BigDecimal("49999.99"), "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30));
        Transaction t2 = new Transaction(10002L, 1L,
                new BigDecimal("50000.00"), "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30));
        Transaction t3 = new Transaction(10003L, 1L,
                new BigDecimal("60000.00"), "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30));
        Rule rule = new LargeAmountRule();
        System.out.println("金额 49999.99 | " + rule.name() + " | 命中: " + rule.hit(t1));
        System.out.println("金额 50000.00 | " + rule.name() + " | 命中: " + rule.hit(t2));
        System.out.println("金额 60000.00 | " + rule.name() + " | 命中: " + rule.hit(t3));
    }
}
