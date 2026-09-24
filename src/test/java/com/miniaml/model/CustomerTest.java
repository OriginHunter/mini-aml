package com.miniaml.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void constructor_setsAllFields() {
        Customer c = new Customer(1L, "张三", "110101199001011234");
        assertEquals(1L, c.getId());
        assertEquals("张三", c.getName());
        assertEquals("110101199001011234", c.getIdCard());
    }

    @Test
    void setters_updateFields() {
        Customer c = new Customer();
        c.setId(2L);
        c.setName("李四");
        c.setIdCard("110101199001015678");
        assertEquals(2L, c.getId());
        assertEquals("李四", c.getName());
        assertEquals("110101199001015678", c.getIdCard());
    }

    @Test
    void toString_containsFields() {
        Customer c = new Customer(1L, "张三", "110101199001011234");
        String s = c.toString();
        assertTrue(s.contains("张三"));
        assertTrue(s.contains("110101199001011234"));
    }
}