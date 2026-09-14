package com.miniaml;

import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.SuspiciousCase;
import com.miniaml.model.Transaction;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("60000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10,30)));
        transactions.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 10, 14,0)));
        transactions.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("150000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 16,0)));
        System.out.println("共 " + transactions.size() +" 笔交易");
        for(Transaction transaction : transactions){
            System.out.println(transaction.getId() + " - " + transaction.getAmount());
        }
    }
}
/*
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
        //创建交易
        Transaction transaction = new Transaction(
                10001L,
                account.getId(),
                new BigDecimal("60000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10,30));

        //打印标题
        System.out.println(
                """
                ========================
                      mini-aml
                   交易监测系统 v0.1
                ========================
                """);
        System.out.println(
                "客户名:" + customer.getName() + "\n" +
                "账户ID:" + account.getAccountNo() + "\n" +
                "交易流水号:" + transaction.getId() + "\n" +
                "金额:" + transaction.getAmount() + "\n" +
                "交易时间:" + transaction.getTransTime() + "\n");

        //判断单笔金额是否命中
        System.out.println("规则：单笔金额大于等于 5 万\n");
        Rule rule = new LargeAmountRule();
        if(rule.hit(transaction)){
            System.out.println("⚠ 命中");
        }
        else{
            System.out.println("未命中");
        }

*/
