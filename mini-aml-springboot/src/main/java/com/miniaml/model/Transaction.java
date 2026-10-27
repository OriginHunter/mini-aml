package com.miniaml.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("transaction") // 告诉 MP 表名
public class Transaction {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;        //交易编号

    @NotNull(message = "账户 ID 不能为空") // 注意这里改成了 @NotNull
    private Long accountId;     //属于哪个账户

    @NotNull(message = "金额不能为空") // 注意这里也改成了 @NotNull
    private BigDecimal amount;      //金额

    private String type;        //进出账类型

    // 【防坑】数据库是 trans_time，Java 是 transTime，必须强绑定
    @TableField("trans_time")
    private LocalDateTime transTime;        //交易时间
}