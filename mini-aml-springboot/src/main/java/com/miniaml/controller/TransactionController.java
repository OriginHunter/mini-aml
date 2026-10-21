package com.miniaml.controller;

import com.miniaml.model.Transaction;
import com.miniaml.service.TransactionService; // 注意这里换了
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
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
}