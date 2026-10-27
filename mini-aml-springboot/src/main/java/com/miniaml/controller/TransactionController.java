package com.miniaml.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    private final TransactionService transactionService;

    // 构造器注入 Service
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public Result<List<Transaction>> getTransactions() {
        List<Transaction> list = transactionService.getAllTransactions();
        return Result.success(list);
    }
    @GetMapping("/transactions/search")
    public Result<List<Transaction>> getByAccountId(@RequestParam Long accountId) {
        List<Transaction> list = transactionService.getTransactionsByAccountId(accountId);
        return Result.success(list);
    }
    @GetMapping("/transactions/large")
    public Result<List<Transaction>> getLargeTransactions(@RequestParam Long accountId) {
        // 调用 Service 刚写的新方法
        List<Transaction> list = transactionService.getLargeTransactionsByAccountId(accountId);
        return Result.success(list);
    }
    @GetMapping("/transactions/page")
    public Result<Page<Transaction>> getTransactionPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long accountId) {

        Page<Transaction> result = transactionService.getTransactionPage(pageNum, pageSize, accountId);
        return Result.success(result);
    }
}