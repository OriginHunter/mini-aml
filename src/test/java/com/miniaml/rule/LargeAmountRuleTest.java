package com.miniaml.rule;

import com.miniaml.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LargeAmountRuleTest {
    @Test
    void 交易60000_应该命中() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("60000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule rule = new LargeAmountRule();
        boolean 命中 = rule.hit(transactions);
        assertTrue(命中);
    }
    @Test
    void 交易49999_99_不应该命中() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("49999.99"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule rule = new LargeAmountRule();
        boolean 命中 = rule.hit(transactions);
        assertFalse(命中);
    }
    @Test
    void 交易50000_应该命中() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("50000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule rule = new LargeAmountRule();
        boolean 命中 = rule.hit(transactions);
        assertTrue(命中);
    }
}
