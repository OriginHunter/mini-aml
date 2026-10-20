package com.miniaml.repository;

import com.miniaml.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    // 构造器注入
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> findAll() {
        // 只查前 10 条测试
        String sql = "SELECT id, account_id, amount, type, trans_time FROM `transaction` LIMIT 10";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Transaction t = new Transaction();
            t.setId(rs.getLong("id"));
            t.setAccountId(rs.getLong("account_id"));
            t.setAmount(rs.getBigDecimal("amount"));
            t.setType(rs.getString("type"));
            t.setTransTime(rs.getTimestamp("trans_time").toLocalDateTime());
            return t;
        });
    }
}