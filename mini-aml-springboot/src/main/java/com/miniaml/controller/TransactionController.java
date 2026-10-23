package com.miniaml.controller;

import com.miniaml.common.Result;
import com.miniaml.model.Transaction;
import com.miniaml.service.TransactionService; // 注意这里换了
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin // 允许跨域请求
public class TransactionController {

    // 以前依赖 Repository，现在依赖 Service
    private final TransactionService service;

    // 构造器注入 Service
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        // 服务员现在不找仓库了，改找大厨
        return service.getAllTransactions();
    }
    @GetMapping("/transactions/search")
    public Result<List<Transaction>> getByAccountId(@RequestParam Long accountId) {
        return Result.success(service.getTransactionsByAccountId(accountId));
    }
    @GetMapping("/transactions/large")
    public Result<List<Transaction>> getLargeTransactions(@RequestParam Long accountId) {
        // 调用 Service 刚写的新方法
        List<Transaction> list = service.getLargeTransactionsByAccountId(accountId);
        return Result.success(list);
    }}