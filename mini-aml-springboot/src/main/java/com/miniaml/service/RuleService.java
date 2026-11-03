package com.miniaml.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miniaml.mapper.RuleConfigMapper;
import com.miniaml.mapper.TransactionMapper;
import com.miniaml.model.RuleConfig;
import com.miniaml.model.SuspiciousCase;
import com.miniaml.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RuleService {

    private final RuleConfigMapper ruleConfigMapper;
    private final SuspiciousCaseService suspiciousCaseService;
    private final TransactionMapper transactionMapper;

    @Transactional(rollbackFor = Exception.class)
    public Integer executeRule(Long id) {

        log.info("获取规则");
        RuleConfig ruleConfig = ruleConfigMapper.selectById(id);

        if (ruleConfig == null || Boolean.FALSE.equals(ruleConfig.getEnabled())) {
            throw new RuntimeException("规则不存在或未启用");
        }
        log.info("获取交易列表");
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        BigDecimal threshold = ruleConfig.getThreshold();
        wrapper.gt(Transaction::getAmount, threshold);
        List<Transaction> transactionList = transactionMapper.selectList(wrapper);

        log.info("获取可疑案例列表");
        String dateStr = DateUtil.format(new Date(), "yyyyMMdd");
        List<SuspiciousCase> suspiciousCaseList = new ArrayList<>();
        for (Transaction transaction : transactionList) {

            SuspiciousCase suspiciousCase = createCaseNo(transaction, dateStr, ruleConfig);
            suspiciousCaseList.add(suspiciousCase);
            // 模拟生产环境的异常场景：如果交易超过 100 条，直接宕机
            if (transactionList.size() > 100) {
                throw new RuntimeException("模拟跑批中途宕机！");
            }
        }
        log.info("开始插入可疑案例列表");
        suspiciousCaseService.saveBatch(suspiciousCaseList);
        log.info("插入可疑案例列表成功");
        return transactionList.size();

    }

    private static @NonNull SuspiciousCase createCaseNo(Transaction transaction, String dateStr, RuleConfig ruleConfig) {
        SuspiciousCase suspiciousCase = new SuspiciousCase();
        String snowflakeId = IdUtil.getSnowflakeNextIdStr();
        String randomStr = snowflakeId.substring(snowflakeId.length() - 8); // 截取最后 8 位
        String caseNo = "AML" + dateStr + "_" + randomStr;

        suspiciousCase.setId(null);
        suspiciousCase.setCaseNo(caseNo);
        suspiciousCase.setTransactionId(transaction.getId());
        suspiciousCase.setRuleName(ruleConfig.getRuleName());
        suspiciousCase.setStatus("NEW");
        return suspiciousCase;
    }

}
