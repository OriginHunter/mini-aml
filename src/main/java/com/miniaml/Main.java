package com.miniaml;

import com.miniaml.dao.TransactionDao;
import com.miniaml.dao.SuspiciousCaseDao;
import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.SuspiciousCase;
import com.miniaml.model.Transaction;
import com.miniaml.rule.DailyAmountRule;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;
import com.miniaml.rule.SmurfingRule;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static long caseCounter = -1;

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

        //新建dao对象
        TransactionDao transactionDao = new TransactionDao();
        SuspiciousCaseDao suspiciousCaseDao = new SuspiciousCaseDao();
        //创建交易列表
        List<Transaction> transactions = transactionDao.findAll();
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
        checkRulesByAccount(byAccount, rules, suspiciousCaseDao);
        // 最后打印所有案例
        System.out.println("\n===== 已生成的可疑案例 =====");
        printCases(suspiciousCaseDao.findAll());
        Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate = groupByAccountDate(byAccount);
        byAccountDate.forEach((id, t) -> System.out.println("账户" + id + ":" + t));
        //把每个账户每天交易金额求和
        sumByAccountDate(byAccountDate);
    }
    private static String nextCaseNo(SuspiciousCaseDao dao) {
        if (caseCounter < 0) {
            caseCounter = dao.findAll().size();
        }
        caseCounter++;
        return String.format("CASE-%04d", caseCounter);
    }

    private static void printCases(List<SuspiciousCase> cases) {
        if (cases.isEmpty()) {
            System.out.println("（暂无案例）");
            return;
        }
        for (SuspiciousCase c : cases) {
            System.out.println(c);
        }
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

    private static void checkRulesByAccount(
            Map<Long, List<Transaction>> byAccount,
            List<Rule<List<Transaction>>> rules,
            SuspiciousCaseDao suspiciousCaseDao) {

        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            Long accountId = entry.getKey();
            List<Transaction> txns = entry.getValue();

            System.out.println("========== 账户 " + accountId + " ==========");
            System.out.println("该账户共有 " + txns.size() + " 笔交易");

            for (Rule<List<Transaction>> rule : rules) {
                List<Transaction> hits = rule.findHits(txns);

                if (hits.isEmpty()) {
                    System.out.println("规则:" + rule.name() + " 未命中");
                } else {
                    System.out.println("规则:" + rule.name() + " ⚠ 命中（" + hits.size() + " 笔）");

                    for (Transaction t : hits) {
                        SuspiciousCase c = new SuspiciousCase(
                                nextCaseNo(suspiciousCaseDao),
                                t.getId(),
                                rule.name(),
                                LocalDateTime.now(),
                                "NEW"
                        );
                        suspiciousCaseDao.insert(c);
                    }
                }
            }
        }
    }}
