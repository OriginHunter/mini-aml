package com.miniaml.service;

import com.miniaml.mapper.RuleConfigMapper;
import com.miniaml.mapper.SuspiciousCaseMapper;
import com.miniaml.mapper.TransactionMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RuleService {

    private final RuleConfigMapper ruleConfigMapper;
    private final SuspiciousCaseMapper suspiciousCaseMapper;
    private final TransactionMapper transactionMapper;

    @Transactional(rollbackFor = Exception.class)
    public void executeRule(Long ruleId) {
        ruleConfigMapper.selectById(ruleId);
    }



}
