package com.miniaml.rule;

import com.miniaml.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyAmountRuleTest {
    @Test
    void hit_singleAmount200000_returnsTrue() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("200000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule<List<Transaction>> rule = new DailyAmountRule();
        boolean hit = rule.hit(transactions);
        assertTrue(hit);
    }
    @Test
    void hit_multipleAmountsOver200000_returnsTrue() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("110000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("120000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 11, 30)));

        Rule<List<Transaction>> rule = new DailyAmountRule();
        boolean hit = rule.hit(transactions);
        assertTrue(hit);
    }
    @Test
    void hit_singleAmount190000_returnsFalse() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("190000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule<List<Transaction>> rule = new DailyAmountRule();
        boolean hit = rule.hit(transactions);
        assertFalse(hit);
    }
}
