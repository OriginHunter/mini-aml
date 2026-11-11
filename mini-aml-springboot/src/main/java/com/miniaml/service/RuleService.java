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
    private final RuleCacheService ruleCacheService;

    /**
     * 读规则配置：先看 Redis，未命中再回源查库并写回缓存。
     * 这样跑批和“查看规则详情”都走同一入口，缓存只有一处维护。
     */
    public RuleConfig getRuleConfig(Long id) {
        return ruleCacheService.get(id).orElseGet(() -> {
            log.info("缓存未命中，回源查库，ruleId={}", id);
            RuleConfig ruleConfig = ruleConfigMapper.selectById(id);
            if (ruleConfig == null) {
                throw new RuntimeException("规则不存在");
            }
            ruleCacheService.put(ruleConfig);
            return ruleConfig;
        });
    }

    /**
     * 修改规则配置（阈值 / 启停）。
     * 顺序很重要：先更新数据库，再删除缓存——不能反过来，
     * 否则删完缓存的瞬间有请求回源，会把旧值又写回缓存。
     */
    public void updateRuleConfig(Long id, BigDecimal threshold, Boolean enabled) {
        RuleConfig ruleConfig = ruleConfigMapper.selectById(id);
        if (ruleConfig == null) {
            throw new RuntimeException("规则不存在");
        }
        if (threshold != null) {
            ruleConfig.setThreshold(threshold);
        }
        if (enabled != null) {
            ruleConfig.setEnabled(enabled);
        }
        ruleConfigMapper.updateById(ruleConfig);
        ruleCacheService.evict(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer executeRule(Long id) {

        log.info("获取规则（走缓存）");
        RuleConfig ruleConfig = getRuleConfig(id);

        if (Boolean.FALSE.equals(ruleConfig.getEnabled())) {
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
        }
        log.info("开始插入可疑案例列表");
        suspiciousCaseService.saveBatch(suspiciousCaseList);
        log.info("插入可疑案例列表成功");
        // 1. 获取当前命中数（防空指针）
        int currentHit = ruleConfig.getHitCount() == null ? 0 : ruleConfig.getHitCount();
        // 2. 累加
        ruleConfig.setHitCount(currentHit + suspiciousCaseList.size());
        // 3. 更新数据库
        ruleConfigMapper.updateById(ruleConfig);
        // 4. 命中数变了，缓存里的副本就是旧值，必须失效，否则前端看到的一直是旧命中数
        ruleCacheService.evict(id);
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
