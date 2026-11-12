package com.miniaml.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 测试结束后自动回滚，不污染数据库
public class RuleServiceTest {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SuspiciousCaseService suspiciousCaseService;

    @Test
    public void testRuleNotFound() {
        // 1. 断言会抛出 RuntimeException
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            // 传入一个不存在的 ID
            ruleService.executeRule(999L);
        });

        // 2. 断言异常信息对不对（改成和业务代码一致的文案）
        Assertions.assertEquals("规则不存在或未启用", exception.getMessage()); // 👈 改这里
    }

    @Test
    public void testTransactionRollback() {
        // 1. 记录执行前的总条数
        long countBefore = suspiciousCaseService.count();

        // 2. 调用跑批接口，因为我们在 Service 里加了 size > 100 抛异常的代码，这里一定会抛异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            ruleService.executeRule(1L); // 传真实存在的规则ID
        });

        // 3. 记录执行后的总条数
        long countAfter = suspiciousCaseService.count();

        // 4. 断言：前后数量一致，证明事务生效了
        Assertions.assertEquals(countBefore, countAfter, "事务没有生效，数据发生了脏写！");
    }
}