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
    void 一笔200000应该命中() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("200000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule rule = new DailyAmountRule();
        boolean 命中 = rule.hit(transactions);
        assertTrue(命中);
    }
    @Test
    void 多笔超过200000应该命中() {
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
                LocalDateTime.of(2026, 9, 11, 10, 30)));

        Rule rule = new DailyAmountRule();
        boolean 命中 = rule.hit(transactions);
        assertTrue(命中);
    }
    @Test
    void 一笔190000不应该命中() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("190000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));

        Rule rule = new DailyAmountRule();
        boolean 命中 = rule.hit(transactions);
        assertFalse(命中);
    }
}
