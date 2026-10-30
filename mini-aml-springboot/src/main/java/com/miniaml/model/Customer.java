package com.miniaml.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户实体类
 */
@Data // 自动生成所有 Getter/Setter/toString 等方法
@NoArgsConstructor // 自动生成无参构造（框架反射需要）
@AllArgsConstructor // 自动生成全参构造
@TableName("`customer`")
public class Customer {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 【业务规则】金融合规要求：客户姓名绝对不能为空
    @NotBlank(message = "客户姓名不能为空")
    private String name;
    // 【业务规则】身份证号是唯一标识，绝对不可为空
    @NotBlank(message = "身份证号不能为空")
    private String idCard;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 逻辑删除标记：0 正常，1 已删除
    @TableLogic
    private Integer deleted;

}
