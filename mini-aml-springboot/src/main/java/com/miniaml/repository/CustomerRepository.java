package com.miniaml.repository;

import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    // 构造器注入
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> findAll() {

        String sql = "SELECT id, name, id_card, created_at FROM Customer;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Customer c = new Customer();
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
            c.setIdCard(rs.getString("id_card"));
            c.setCreateTime(rs.getTimestamp("created_at").toLocalDateTime());
            return c;
        });
    }
    public List<Customer> getByName(String name) {

        String sql = "SELECT id, name, id_card, created_at FROM Customer WHERE name = ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Customer c = new Customer();
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
            c.setIdCard(rs.getString("id_card"));
            c.setCreateTime(rs.getTimestamp("created_at").toLocalDateTime());
            return c;
        },name);
    }
}
