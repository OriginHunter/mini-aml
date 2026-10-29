package com.miniaml.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDetailDTO {
    private Long id;               // 交易ID
    private String customerName;   // 客户姓名 (来自 customer 表)
    private String accountNo;      // 账户卡号 (来自 account 表)
    private BigDecimal amount;     // 交易金额 (来自 transaction 表)
    private String type;           // 交易类型
    private LocalDateTime transTime; // 交易时间
}