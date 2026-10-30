package com.miniaml.model;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("`transaction`") // 告诉 MP 表名
public class Transaction {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;        //交易编号

    @NotNull(message = "账户 ID 不能为空") // 注意这里改成了 @NotNull
    private Long accountId;     //属于哪个账户
    @NotNull(message = "金额不能为空") // 注意这里也改成了 @NotNull
    private BigDecimal amount;      //金额
    private String type;        //进出账类型
    // 【防坑】数据库是 trans_time，Java 是 transTime，必须强绑定
    @TableField(value = "trans_time", fill = FieldFill.INSERT)
    private LocalDateTime transTime;        //交易时间

    // 逻辑删除标记：0 正常，1 已删除
    @TableLogic
    private Integer deleted;

}
