package com.miniaml.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor // 自动生成无参构造（框架反射需要）
@AllArgsConstructor // 自动生成全参构造
@TableName("suspicious_case")
public class SuspiciousCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String caseNo;
    private Long transactionId;
    private String ruleName;
    private String status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

}