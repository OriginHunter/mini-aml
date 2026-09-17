package com.miniaml.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {
    @Test
    void 正常无异常() {
        assertDoesNotThrow( () -> new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }
    @Test
    void 金额负数有异常() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                10001L,
                1L,
                new BigDecimal("-40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }
    @Test
    void 交易类型有异常() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "TRANSFER",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }
    @Test
    void 交易ID为空有异常() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                null,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }

}
