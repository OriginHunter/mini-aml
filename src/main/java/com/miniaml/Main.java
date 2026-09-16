package com.miniaml;

import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;
import com.miniaml.rule.DailyAmountRule;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
        List<Transaction> transactions = createTransactions();
        //打印标题
        printHeader();
        //打印客户与账户
        printCustomerInfo(customer, account);
        //打印交易
        printTransactions(transactions);
        //规则列表
        List<Rule> rules = createRules();
        //按照账户给交易分组
        Map<Long, List<Transaction>> byAccount = groupByAccount(transactions);
        //判断规则是否命中
        checkRulesByAccount(byAccount, rules);
        //异常测试
        testException();
        //集合测试
        testSet();
        //fail-fast测试
        testFailFast();
    }
    private static void testFailFast() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        try {
            for (String s : list) {
                if (s.equals("B")) {
                    list.remove(s);      // 触发异常
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("捕获到：" + e.getClass().getSimpleName());
        }
    }
    private static void testSet(){
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        set.add("A");
        System.out.println("集合大小" + set.size());
        System.out.println("包含 A " + set.contains("A"));
        for (String s : set) {
            System.out.println(s);
        }
    }
    private static List<Transaction> createTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10, 30)));
        transactions.add(new Transaction(
                10002L,
                2L,
                new BigDecimal("180000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 23, 30)));
        transactions.add(new Transaction(
                10003L,
                2L,
                new BigDecimal("30000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 11, 6, 30)));
        transactions.add(new Transaction(
                10004L,
                1L,
                new BigDecimal("20000.00"),
                "IN",
                LocalDateTime.of(2026, 10, 10, 10, 30)));
        transactions.add(new Transaction(
                10005L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 10, 10, 15, 30)));
        return transactions;
    }

    private static List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new LargeAmountRule());
        rules.add(new DailyAmountRule());
        return rules;
    }

    private static void printHeader() {
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
                            "金额:" + transaction.getAmount() + "\n" +
                            "交易时间:" + transaction.getTransTime() + "\n");
        }
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

    private static Map<Long, List<Transaction>> groupByAccount(List<Transaction> transactions) {
        Map<Long, List<Transaction>> byAccount = new HashMap<>();
        for (Transaction transaction : transactions) {
            Long accountId = transaction.getAccountId();
            if (!byAccount.containsKey(accountId)) {
                byAccount.put(accountId, new ArrayList<>());
            }
            byAccount.get(accountId).add(transaction);
        }
        return byAccount;
    }

    private static void checkRulesByAccount(Map<Long, List<Transaction>> byAccount, List<Rule> rules) {
        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            System.out.println("========== 账户 " + entry.getKey() + " ==========");
            System.out.println("该账户共有 " + entry.getValue().size() + " 笔交易");
            for (Rule rule : rules) {
                if (rule.hit(entry.getValue())) {
                    System.out.println("规则:" + rule.name() + " ⚠ 命中");
                } else {
                    System.out.println("规则:" + rule.name() + " 未命中");
                }
            }
        }
    }

}


/*


 */
