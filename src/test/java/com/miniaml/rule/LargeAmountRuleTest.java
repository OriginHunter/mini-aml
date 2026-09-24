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
    void hit_amount60000_returnsTrue() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("60000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule<List<Transaction>> rule = new LargeAmountRule();
        boolean hit = rule.hit(transactions);
        assertTrue(hit);
    }
    @Test
    void hit_amount49999_99_returnsFalse() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("49999.99"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule<List<Transaction>> rule = new LargeAmountRule();
        boolean hit = rule.hit(transactions);
        assertFalse(hit);
    }
    @Test
    void hit_amount50000_returnsTrue() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("50000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule<List<Transaction>> rule = new LargeAmountRule();
        boolean hit = rule.hit(transactions);
        assertTrue(hit);
    }
}
