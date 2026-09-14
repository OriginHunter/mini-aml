package com.miniaml;

import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.SuspiciousCase;
import com.miniaml.model.Transaction;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        SuspiciousCase s = new SuspiciousCase(
                "CASE-1",
                10001L,
                "单笔金额大于等于 5 万",
                LocalDateTime.of(2026, 9, 10, 10, 30),
                "NEW"
                );
        System.out.println(s.getCaseNo());
        System.out.println(s.getTransactionId());
        System.out.println(s.getRuleName());
        System.out.println(s.getCreateTime());
        System.out.println(s.getStatus());
        s.setCaseNo("CASE-2");
        s.setTransactionId(10002L);
        s.setRuleName("单日累计大于等于 20 万");
        s.setCreateTime(LocalDateTime.of(2026, 9, 14, 16, 30));
        s.setStatus("CONFIRMED");
        System.out.println(s);
/*        //创建客户
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
        }*/

    }
}
