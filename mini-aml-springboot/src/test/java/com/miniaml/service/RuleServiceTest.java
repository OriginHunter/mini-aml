package com.miniaml.service;

import lombok.AllArgsConstructor;
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

    // 因为我们要查“可疑案例”的总数，所以要把 SuspiciousCaseService 也注入进来
    @Autowired
    private SuspiciousCaseService suspiciousCaseService; // 提示：把下划线替换成 SuspiciousCaseService

    @Test
    public void testRuleNotFound() {
        // 1. 断言会抛出 RuntimeException
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            // 这里调用方法，传一个不存在的 ID，比如 99999999L
            ruleService.executeRule(999L);
        });

        // 2. 断言异常信息对不对
        Assertions.assertEquals("规则不存在或未启用", exception.getMessage());
    }

    @Test
    public void testTransactionRollback() {
        // 1. 记录执行前的总条数
        long countBefore = suspiciousCaseService.count(); // 提示：MP的IService里，哪个方法能统计总数？填那个方法名

        // 2. 调用跑批接口，因为我们在 Service 里加了 size > 100 抛异常的代码，这里一定会抛异常
        Assertions.assertThrows(RuntimeException.class, () -> {
            // 把 你的真实规则ID_L 换成你刚才在数据库里查到的真实 ID
            ruleService.executeRule(1L);
        });

        // 3. 记录执行后的总条数
        long countAfter = suspiciousCaseService.count();

        // 4. 断言：前后数量一致，证明哪怕在循环里组装了对象，只要报错，事务就回滚了，一条都没插进去
        Assertions.assertEquals(countBefore, countAfter, "事务没有生效，数据发生了脏写！");
    }
}