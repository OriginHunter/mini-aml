package com.miniaml.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuspiciousCaseVO {
    private String caseNo;        // 案例号
    private String ruleName;      // 规则名
    private BigDecimal amount;    // 交易金额
    private String accountNo;     // 账号
    private String customerName;  // 客户姓名
}
