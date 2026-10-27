package com.miniaml.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniaml.mapper.TransactionMapper;
import com.miniaml.model.Transaction;
import com.miniaml.rule.LargeAmountRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service // 告诉 Spring：这是一个 Service 层的 Bean
@RequiredArgsConstructor // 使用 Lombok 自动生成构造器
public class TransactionService {

    private final LargeAmountRule largeAmountRule;
    private final TransactionMapper transactionMapper;

    public List<Transaction> getAllTransactions() {
        return transactionMapper.selectList(null);
    }

    public Page<Transaction> getTransactionPage(
            Integer pageNum, Integer pageSize, Long accountId,
            // TODO 5: 接收 Controller 传来的三个新参数
            BigDecimal minAmount, BigDecimal maxAmount, String type) {

        Page<Transaction> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();

        // 1. 按账户查（这个你写过了）
        wrapper.eq(accountId != null, Transaction::getAccountId, accountId);

        // TODO 6: 按最小金额查（大于等于）。提示：用 .ge() 方法
        wrapper.ge(minAmount != null, Transaction::getAmount, minAmount);

        // TODO 7: 按最大金额查（小于等于）。提示：用 .le() 方法
        wrapper.le(maxAmount != null, Transaction::getAmount, maxAmount);

        // TODO 8: 按类型查（等于）。提示：判断 type 不为 null 且不为空字符串 ""
        wrapper.eq(type != null && !type.isEmpty(), Transaction::getType, type);

        return transactionMapper.selectPage(page, wrapper);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getAccountId, accountId);
        return transactionMapper.selectList(wrapper);
    }
    public List<Transaction> getLargeTransactionsByAccountId(Long accountId) {
        // 记录一条 INFO 级别的日志，{} 是占位符，会自动把 accountId 填进去
        log.info("开始查询账户 {} 的大额交易记录", accountId);

        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getAccountId, accountId);

        List<Transaction> allTransactionsByAccountId = transactionMapper.selectList(wrapper);
        List<Transaction> hits = largeAmountRule.findHits(allTransactionsByAccountId);

        log.info("查询完成，共查出 {} 条大额交易", hits.size());
        return hits;
    }

}