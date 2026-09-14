package com.miniaml.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    //写五个字段
    private Long id;        //交易编号
    private Long accountId;     //属于哪个账户
    private BigDecimal amount;      //金额
    private String type;        //进出账类型
    private LocalDateTime transTime;        //交易时间

    //两种构造函数
    public Transaction() {
    }

    public Transaction(Long id,
                       Long accountId,
                       BigDecimal amount,
                       String type,
                       LocalDateTime transTime) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.transTime = transTime;
    }

    //10个方法读写字段
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTransTime() {
        return transTime;
    }

    public void setTransTime(LocalDateTime transTime) {
        this.transTime = transTime;
    }

    //toString()方法重写
    @Override
    public String toString() {
        return "Transaction{id=" + id +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", type='" + type +
                "', transTime=" + transTime +
                "}";
    }
}

/*示例1
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.miniaml.model.Transaction;

public class Main {
    public static void main(String[] args) {
        Transaction t = new Transaction(
                10001L,
                1L,
                new BigDecimal("60000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 10,30));
        System.out.println(t.getId());
        System.out.println(t.getAccountId());
        System.out.println(t.getAmount());
        System.out.println(t.getType());
        System.out.println(t.getTransTime());
        System.out.println(t);
        t.setId(10002L);
        t.setAccountId(2L);
        t.setAmount(new BigDecimal("30000.00"));
        t.setType("OUT");
        t.setTransTime(LocalDateTime.of(2026, 9, 14, 9, 50));
        System.out.println(t);

    }
}
*/
/*示例2
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.miniaml.model.Transaction;

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
*/
