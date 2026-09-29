package com.miniaml.dao;

import com.miniaml.model.Transaction;
import com.miniaml.util.DbConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private final String url = DbConfig.getUrl();
    private final String user = DbConfig.getUser();
    private final String password = DbConfig.getPassword();

    public List<Transaction> findAll() {

        String sql = "SELECT id, account_id, amount, type, trans_time FROM transactions";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                Long accountId = rs.getLong("account_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                String type = rs.getString("type");
                LocalDateTime transTime = rs.getTimestamp("trans_time").toLocalDateTime();

                transactions.add(new Transaction(id, accountId, amount, type, transTime));
            }

        } catch (SQLException e) {
            System.out.println("数据库读取失败：" + e.getMessage());
        }

        return transactions;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int rows = stmt.executeUpdate();
            System.out.println("删除成功，影响行数：" + rows);

        } catch (SQLException e) {
            System.out.println("删除失败：" + e.getMessage());
        }
    }

    public void updateAmount(Long id, BigDecimal newAmount) {
        String sql = "UPDATE transactions SET amount = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, newAmount);   // 第一个 ?
            stmt.setLong(2, id);                 // 第二个 ?

            int rows = stmt.executeUpdate();
            System.out.println("更改成功，影响行数：" + rows);

        } catch (SQLException e) {
            System.out.println("更改失败：" + e.getMessage());
        }
    }

    public void insertTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (id, account_id, amount, type, trans_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, t.getId());
            stmt.setLong(2, t.getAccountId());
            stmt.setBigDecimal(3, t.getAmount());
            stmt.setString(4, t.getType());
            stmt.setTimestamp(5, Timestamp.valueOf(t.getTransTime()));

            int rows = stmt.executeUpdate();
            System.out.println("插入成功，影响行数：" + rows);

        } catch (SQLException e) {
            System.out.println("插入失败：" + e.getMessage());
        }
    }

    public void insertBatch(List<Transaction> transactions) {
        String sql = "INSERT INTO transactions (id, account_id, amount, type, trans_time) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            // ① 关掉自动提交
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // ② 循环插入
                for (Transaction t : transactions) {
                    stmt.setLong(1, t.getId());
                    stmt.setLong(2, t.getAccountId());
                    stmt.setBigDecimal(3, t.getAmount());
                    stmt.setString(4, t.getType());
                    stmt.setTimestamp(5, Timestamp.valueOf(t.getTransTime()));
                    stmt.executeUpdate();
                }

                // ③ 全部成功，提交
                conn.commit();
                System.out.println("批量插入成功，共 " + transactions.size() + " 条");
            }
        } catch (SQLException e) {
            // 出错回滚
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("批量插入失败，已回滚：" + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
