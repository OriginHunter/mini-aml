package com.miniaml.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SuspiciousCaseTest {
    @Test
    void constructor_setsAllFields() {
        SuspiciousCase s = new SuspiciousCase(
                "CASE-1",
                10001L,
                "单笔金额大于等于 5 万",
                LocalDateTime.of(2026, 9, 10, 10, 30),
                "NEW"
        );

        assertEquals("CASE-1", s.getCaseNo());
        assertEquals(10001L, s.getTransactionId());
        assertEquals("单笔金额大于等于 5 万", s.getRuleName());
        assertEquals(LocalDateTime.of(2026, 9, 10, 10, 30), s.getCreateTime());
        assertEquals("NEW", s.getStatus());
    }

    @Test
    void setters_updateFields() {
        SuspiciousCase s = new SuspiciousCase();
        s.setCaseNo("CASE-2");
        s.setTransactionId(10002L);
        s.setRuleName("单日累计大于等于 20 万");
        s.setCreateTime(LocalDateTime.of(2026, 9, 11, 10, 30));
        s.setStatus("NEW");

        assertEquals("CASE-2", s.getCaseNo());
        assertEquals(10002L, s.getTransactionId());
        assertEquals("单日累计大于等于 20 万", s.getRuleName());
        assertEquals(LocalDateTime.of(2026, 9, 11, 10, 30), s.getCreateTime());
        assertEquals("NEW", s.getStatus());
    }

    @Test
    void toString_containsFields() {
        SuspiciousCase s = new SuspiciousCase(
                "CASE-1",
                10001L,
                "单笔金额大于等于 5 万",
                LocalDateTime.of(2026, 9, 10, 10, 30),
                "NEW"
        );

        String string = s.toString();
        assertTrue(string.contains("CASE-1"));
        assertTrue(string.contains("单笔金额大于等于 5 万"));
        assertTrue(string.contains("NEW"));
    }

}
