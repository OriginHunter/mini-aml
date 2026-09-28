package com.miniaml;

import com.miniaml.exception.InvalidTransactionException;
import com.miniaml.learning.LambdaExamples;
import com.miniaml.learning.StreamExamples;
import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;
import com.miniaml.rule.DailyAmountRule;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;
import com.miniaml.rule.SmurfingRule;
import com.miniaml.util.DbConfig;
import com.miniaml.util.ListUtil;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //创建客户
        Customer customer = new Customer(
                1L,
                "张三",
                "110101199001011234");
        //创建账户
        Account account = new Account(
                1L,
                "622200001",
                customer.getId(),
                new BigDecimal("100000.00"));
        try {
            Transaction newT = new Transaction(
                    99999L, 1L,
                    new BigDecimal("88888.00"),
                    "IN",
                    LocalDateTime.of(2026, 9, 28, 16, 30));
            //增添新交易
            insertTransaction(newT);
            //更改交易
            updateAmount(99999L, new BigDecimal("12345.00"));
            //创建交易列表
            List<Transaction> transactions = loadTransactionsFromDb();
            //打印标题
            printHeader();
            //打印客户与账户
            printCustomerInfo(customer, account);
            //打印交易
            printTransactions(transactions);
            //规则列表
            List<Rule<List<Transaction>>> rules = createRules();
            //按照账户给交易分组
            Map<Long, List<Transaction>> byAccount = groupByAccount(transactions);
            //判断规则是否命中
            checkRulesByAccount(byAccount, rules);
            //把按照账户分组的交易按日期分组
            Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate = groupByAccountDate(byAccount);
            byAccountDate.forEach((id, t) -> System.out.println("账户" + id + ":" + t));
            //把每个账户每天交易金额求和
            sumByAccountDate(byAccountDate);
            //连续三天40000~50000测试
            testSmurfingTransaction();
            //删除交易
            deleteById(99999L);
        } catch (InvalidTransactionException e) {
            System.out.println("数据错误：" + e.getMessage());
        }
    }

    private static void deleteById(Long id) {
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
    private static void updateAmount(Long id, BigDecimal newAmount) {
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

    private static void insertTransaction(Transaction t) {
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

    private static List<Transaction> loadTransactionsFromDb() {
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

    private static void testSmurfingTransaction() {
        List<Transaction> transactions1 = new ArrayList<>();
        List<Transaction> transactions2 = new ArrayList<>();
        List<Transaction> transactions3 = new ArrayList<>();
        List<Transaction> transactions4 = new ArrayList<>();
        transactions1.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 1, 10,30)));
        transactions1.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 2, 14,0)));
        transactions1.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 3, 16,0)));
        transactions2.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 7, 10,30)));
        transactions2.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 8, 14,0)));
        transactions2.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 16,0)));
        transactions3.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 11, 10,30)));
        transactions3.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 12, 14,0)));
        transactions3.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("50000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 13, 16,0)));
        transactions4.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 11, 10,30)));
        transactions4.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("30000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 12, 14,0)));
        transactions4.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 13, 16,0)));        //规则列表
        List<Rule<List<Transaction>>> rules = createRules();
        //按照账户给交易分组
        Map<Long, List<Transaction>> byAccount1 = groupByAccount(transactions1);
        Map<Long, List<Transaction>> byAccount2 = groupByAccount(transactions2);
        Map<Long, List<Transaction>> byAccount3 = groupByAccount(transactions3);
        Map<Long, List<Transaction>> byAccount4 = groupByAccount(transactions4);
        //判断规则是否命中
        System.out.println("===== 场景 1：连续 3 天 4 万 =====");
        checkRulesByAccount(byAccount1, rules);

        System.out.println("===== 场景 2：不连续 =====");
        checkRulesByAccount(byAccount2, rules);

        System.out.println("===== 场景 3：有一天 5 万 =====");
        checkRulesByAccount(byAccount3, rules);

        System.out.println("===== 场景 4：有一天 3 万 =====");
        checkRulesByAccount(byAccount4, rules);
    }

    private static void sumByAccountDate(Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate) {
        for (Map.Entry<Long, Map<LocalDate, List<Transaction>>> byAccountDateEntry : byAccountDate.entrySet()) {
            System.out.println(" id :" + byAccountDateEntry.getKey());
            for (Map.Entry<LocalDate, List<Transaction>> byDateEntry : byAccountDateEntry.getValue().entrySet()) {

                System.out.println(" 日期:" + byDateEntry.getKey() + "金额" + sumTransactions(byDateEntry.getValue()));
            }
        }
    }

    private static BigDecimal sumTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add));
    }

    private static Map<Long, Map<LocalDate, List<Transaction>>> groupByAccountDate(Map<Long, List<Transaction>> byAccount) {
        Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate = new HashMap<Long, Map<LocalDate, List<Transaction>>>();
        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            byAccountDate.put(entry.getKey(), groupByDate(entry.getValue()));
        }
        return byAccountDate;
    }

    private static Map<LocalDate, List<Transaction>> groupByDate(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()));
    }


    private static List<Rule<List<Transaction>>> createRules() {
        List<Rule<List<Transaction>>> rules = new ArrayList<>();
        rules.add(new LargeAmountRule());
        rules.add(new DailyAmountRule());
        rules.add(new SmurfingRule());
        return rules;
    }

    private static void printHeader() {
        System.out.println("运行时间：" + LocalDateTime.now());
        System.out.println("""
                ========================
                      mini-aml
                   交易监测系统 v0.1
                ========================
                """);
    }

    private static void printCustomerInfo(Customer customer, Account account) {
        System.out.println(
                "客户名:" + customer.getName() + "\n" +
                        "账户ID:" + account.getAccountNo() + "\n");
    }

    private static void printTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(
                    "交易流水号:" + transaction.getId() + "\n" +
                            "交易账户:" + transaction.getAccountId() + "\n" +
                            "金额:" + transaction.getAmount() + "\n" +
                            "交易时间:" + transaction.getTransTime() + "\n");
        }
    }

    private static Map<Long, List<Transaction>> groupByAccount(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));
    }

    private static void checkRulesByAccount(Map<Long, List<Transaction>> byAccount, List<Rule<List<Transaction>>> rules) {
        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            System.out.println("========== 账户 " + entry.getKey() + " ==========");
            System.out.println("该账户共有 " + entry.getValue().size() + " 笔交易");
            for (Rule<List<Transaction>> rule : rules) {
                if (rule.hit(entry.getValue())) {
                    System.out.println("规则:" + rule.name() + " ⚠ 命中");
                } else {
                    System.out.println("规则:" + rule.name() + " 未命中");
                }
            }
        }
    }
}
