package com.miniaml.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`account`")
public class Account {

    @TableLogic
    private Integer deleted;
    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;
    @NotBlank(message = "帐号不能为空")
    private String accountNo;
    @NotNull(message = "帐户 ID 不能为空")
    private Long customerId;

    private BigDecimal balance;

    private LocalDateTime createdAt;
}
