package com.miniaml.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {
    @Test
    void validData_doesNotThrow() {
        assertDoesNotThrow( () -> new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }
    @Test
    void negativeAmount_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                10001L,
                1L,
                new BigDecimal("-40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }
    @Test
    void invalidType_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "TRANSFER",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }
    @Test
    void nullId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(
                null,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
    }

}
