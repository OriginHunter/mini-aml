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
}
