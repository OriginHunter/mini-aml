package com.miniaml.dto;

import java.math.BigDecimal;

/**
 * 修改规则配置的请求体。
 * 两个字段都可空：只传 threshold 就是改阈值，只传 enabled 就是启停规则。
 *
 * <p>用 record 而不是普通类：DTO 本来就该是不可变的数据载体，
 * record 自带构造器/getter/equals，省得写一堆样板代码。
 */
public record RuleUpdateRequest(BigDecimal threshold, Boolean enabled) {
}
