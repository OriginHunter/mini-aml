package com.miniaml.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`account`")
public class Account {

    @TableId(type = IdType.ASSIGN_ID) // 主键自增
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotBlank(message = "帐号不能为空")
    private String accountNo;
    @NotNull(message = "帐户 ID 不能为空")
    private Long customerId;
    private BigDecimal balance;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableLogic
    private Integer deleted;

}
