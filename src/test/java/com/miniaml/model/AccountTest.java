package com.miniaml.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    void constructor_setsAllFields() {
        Account account = new Account(1L, "66666666", 1L, new BigDecimal("30000"));
        assertEquals(1L, account.getId());
        assertEquals("66666666", account.getAccountNo());
        assertEquals(1L, account.getCustomerId());
        assertEquals(new BigDecimal("30000"), account.getBalance());
    }

    @Test
    void setters_updateFields() {
        Account account = new Account();
        account.setId(2L);
        account.setAccountNo("77777777");
        account.setCustomerId(3L);
        account.setBalance(new BigDecimal("50000"));

        assertEquals(2L, account.getId());
        assertEquals("77777777", account.getAccountNo());
        assertEquals(3L, account.getCustomerId());
        assertEquals(new BigDecimal("50000"), account.getBalance());
    }

    @Test
    void toString_containsFields() {
        Account account = new Account(1L, "66666666", 1L, new BigDecimal("30000"));
        String s = account.toString();
        assertTrue(s.contains("66666666"));
        assertTrue(s.contains("30000"));

    }
}
