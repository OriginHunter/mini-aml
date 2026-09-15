package com.miniaml;

import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;
import com.miniaml.rule.DailyAmountRule;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //创建交易列表
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
        transactions.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 10, 14, 0)));
        transactions.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("150000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 16, 0)));

        //打印标题
        System.out.println("""
                        ========================
                              mini-aml
                           交易监测系统 v0.1
                        ========================
                """);
        System.out.println(
                "客户名:" + customer.getName() + "\n" +
                        "账户ID:" + account.getAccountNo() + "\n");
        for (Transaction transaction : transactions) {
            System.out.println(
                    "交易流水号:" + transaction.getId() + "\n" +
                            "金额:" + transaction.getAmount() + "\n" +
                            "交易时间:" + transaction.getTransTime() + "\n");
        }
        //规则列表
        List<Rule> rules = new ArrayList<>();
        rules.add(new LargeAmountRule());
        rules.add(new DailyAmountRule());
        //判断规则是否命中
        for (Rule rule : rules) {
            System.out.println("规则:" + rule.name());
            if (rule.hit(transactions)) {
                System.out.println(" ⚠ 命中");
            } else {
                System.out.println("未命中");
            }
        }
        //异常测试
        testException();
        //临时测试
        testMap();
    }

    private static void testException() {
        try {
            Transaction t = new Transaction(
                    10001L,
                    1L,
                    new BigDecimal("40000.00"),
                    "IN",
                    LocalDateTime.of(2026, 9, 10, 10, 30));
            System.out.println("创建成功:" + t.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常:" + e.getMessage());
        }
        //金额负数
        try {
            new Transaction(
                    10001L,
                    1L,
                    new BigDecimal("-40000.00"),
                    "IN",
                    LocalDateTime.of(2026, 9, 10, 10, 30));
            System.out.println("不应该到这行");
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常:" + e.getMessage());
        }
        //交易类型异常
        try {
            new Transaction(
                    10001L,
                    1L,
                    new BigDecimal("40000.00"),
                    "TRANSFER",
                    LocalDateTime.of(2026, 9, 10, 10, 30));
            System.out.println("不应该到这行");
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常:" + e.getMessage());
        }
        //交易 ID 为空
        try {
            new Transaction(
                    null,
                    1L,
                    new BigDecimal("40000.00"),
                    "IN",
                    LocalDateTime.of(2026, 9, 10, 10, 30));
            System.out.println("不应该到这行");
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常:" + e.getMessage());
        }

    }

    private static void testMap() {
        Map<Long, String> students = new HashMap<>();
        students.put(2024001L, "张三");
        students.put(2024002L, "李四");
        students.put(2024003L, "王五");
        //遍历students
        for (Long key : students.keySet()) {
            String value = students.get(key);
            System.out.println(key + " → " + value);
        }
        for (Map.Entry<Long, String> entry : students.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

