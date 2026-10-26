package com.miniaml.repository;

import com.miniaml.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    /**
     * 根据 ID 更新客户信息
     */
    public int update(Customer customer) {
        String sql = "UPDATE customer SET name = ?, id_card = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                customer.getName(),
                customer.getIdCard(),
                customer.getId());
    }

    /**
     * 根据 ID 删除客户
     */
    public int deleteById(Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    /**
     * 新增客户记录
     */
    public int insert(Customer customer) {
        // 把 SQL 里的 id 删掉，让它自增
        String sql = "INSERT INTO customer (name, id_card, created_at) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                customer.getName(),
                customer.getIdCard(),
                LocalDateTime.now());
    }
}
