package com.miniaml.dao;

import com.miniaml.model.Transaction;
import com.miniaml.util.DbConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    public List<Transaction> findAll() {
        String url = DbConfig.getUrl();
        String user = DbConfig.getUser();
        String password = DbConfig.getPassword();

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
        String url = DbConfig.getUrl();
        String user = DbConfig.getUser();
        String password = DbConfig.getPassword();

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
        String url = DbConfig.getUrl();
        String user = DbConfig.getUser();
        String password = DbConfig.getPassword();

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
        String url = DbConfig.getUrl();
        String user = DbConfig.getUser();
        String password = DbConfig.getPassword();

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
}
