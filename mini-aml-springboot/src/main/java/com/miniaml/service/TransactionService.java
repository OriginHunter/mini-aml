package com.miniaml.service;

import com.miniaml.exception.GlobalExceptionHandler;
import com.miniaml.model.Transaction;
import com.miniaml.repository.TransactionRepository;
import com.miniaml.rule.LargeAmountRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 告诉 Spring：这是一个 Service 层的 Bean
public class TransactionService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionService.class);    private final TransactionRepository repository;
    private final LargeAmountRule largeAmountRule;

    // 构造器注入：Spring 启动时，会自动把这个 repository 传进来
    public TransactionService(TransactionRepository repository,LargeAmountRule largeAmountRule) {
        this.repository = repository; // 这句你学过的：把传进来的参数，赋值给类自己的属性
        this.largeAmountRule = largeAmountRule;
    }

    // 真正提供给 Controller 调用的方法
    public List<Transaction> getAllTransactions() {
        // 这里就是普通的 Java 方法调用，调用 repository 的 findAll 方法
        return repository.findAll();
    }
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return repository.findByAccountId(accountId);
    }
    public List<Transaction> getLargeTransactionsByAccountId(Long accountId) {
        // 记录一条 INFO 级别的日志，{} 是占位符，会自动把 accountId 填进去
        log.info("开始查询账户 {} 的大额交易记录", accountId);

        List<Transaction> allTransactions = repository.findByAccountId(accountId);
        List<Transaction> hits = largeAmountRule.findHits(allTransactions);

        log.info("查询完成，共查出 {} 条大额交易", hits.size());
        return hits;
    }

}