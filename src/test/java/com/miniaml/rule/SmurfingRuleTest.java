package com.miniaml.rule;

import com.miniaml.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmurfingRuleTest {

    private final SmurfingRule rule = new SmurfingRule();

    private Transaction txn(LocalDateTime time, String amount) {
        return new Transaction(1L, 1L, new BigDecimal(amount), "IN", time);
    }

    @Test
    void hit_threeConsecutiveDaysInRange_returnsTrue() {
        List<Transaction> list = List.of(
                txn(LocalDateTime.of(2026, 9, 1, 10, 0), "40000"),
                txn(LocalDateTime.of(2026, 9, 2, 10, 0), "45000"),
                txn(LocalDateTime.of(2026, 9, 3, 10, 0), "49000")
        );
        assertTrue(rule.hit(list));
    }

    @Test
    void hit_nonConsecutiveDays_returnsFalse() {
        // 09-01, 09-02, 09-04
        List<Transaction> list = List.of(
                txn(LocalDateTime.of(2026, 9, 1, 10, 0), "40000"),
                txn(LocalDateTime.of(2026, 9, 2, 10, 0), "45000"),
                txn(LocalDateTime.of(2026, 9, 4, 10, 0), "49000")
        );
        // 断言 false
        assertFalse(rule.hit(list));
    }

    @Test
    void hit_oneDayEquals50000_returnsFalse() {
        // 09-01, 09-02, 09-03，其中一天 50000（不含 50000）
        List<Transaction> list = List.of(
                txn(LocalDateTime.of(2026, 9, 1, 10, 0), "40000"),
                txn(LocalDateTime.of(2026, 9, 2, 10, 0), "45000"),
                txn(LocalDateTime.of(2026, 9, 3, 10, 0), "50000")
        );
        // 断言 false
        assertFalse(rule.hit(list));
    }

    @Test
    void hit_oneDayBelow40000_returnsFalse() {
        // 09-01, 09-02, 09-03，其中一天 30000
        List<Transaction> list = List.of(
                txn(LocalDateTime.of(2026, 9, 1, 10, 0), "40000"),
                txn(LocalDateTime.of(2026, 9, 2, 10, 0), "45000"),
                txn(LocalDateTime.of(2026, 9, 4, 10, 0), "30000")
        );
        // 断言 false
        assertFalse(rule.hit(list));
    }

    @Test
    void hit_onlyTwoDays_returnsFalse() {
        // 只有 2 笔交易（09-01, 09-02）
        List<Transaction> list = List.of(
                txn(LocalDateTime.of(2026, 9, 1, 10, 0), "40000"),
                txn(LocalDateTime.of(2026, 9, 2, 10, 0), "45000")
        );
        // 断言 false
        assertFalse(rule.hit(list));
    }

    @Test
    void hit_emptyList_returnsFalse() {
        // 空列表
        List<Transaction> list = new ArrayList<>();
        // 断言 false
        assertFalse(rule.hit(list));
    }
}