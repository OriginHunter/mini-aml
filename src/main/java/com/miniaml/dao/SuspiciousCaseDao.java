package com.miniaml.dao;

import com.miniaml.model.SuspiciousCase;
import com.miniaml.util.DbConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuspiciousCaseDao {

    public void insert(SuspiciousCase c) {
        String url = DbConfig.getUrl();
        String user = DbConfig.getUser();
        String password = DbConfig.getPassword();

        String sql = "INSERT INTO suspicious_case (case_no, transaction_id, rule_name, status, create_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getCaseNo());
            stmt.setLong(2, c.getTransactionId());
            stmt.setString(3, c.getRuleName());
            stmt.setString(4, c.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(c.getCreateTime()));

            int rows = stmt.executeUpdate();
            System.out.println("案例入库成功，影响行数：" + rows);

        } catch (SQLException e) {
            System.out.println("案例入库失败：" + e.getMessage());
        }
    }

    public List<SuspiciousCase> findAll() {
        String url = DbConfig.getUrl();
        String user = DbConfig.getUser();
        String password = DbConfig.getPassword();

        String sql = "SELECT id, case_no, transaction_id, rule_name, status, create_time FROM suspicious_case ORDER BY id DESC";

        List<SuspiciousCase> cases = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String caseNo = rs.getString("case_no");
                Long transactionId = rs.getLong("transaction_id");
                String ruleName = rs.getString("rule_name");
                String status = rs.getString("status");
                LocalDateTime createTime = rs.getTimestamp("create_time").toLocalDateTime();

                cases.add(new SuspiciousCase(caseNo, transactionId, ruleName, createTime, status));
            }
        } catch (SQLException e) {
            System.out.println("查询案例失败：" + e.getMessage());
        }

        return cases;
    }
}