package com.miniaml.service;

import com.miniaml.model.Transaction;
import com.miniaml.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 告诉 Spring：这是一个 Service 层的 Bean
public class TransactionService {

    private final TransactionRepository repository;

    // 构造器注入：Spring 启动时，会自动把这个 repository 传进来
    public TransactionService(TransactionRepository repository) {
        this.repository = repository; // 这句你学过的：把传进来的参数，赋值给类自己的属性
    }

    // 真正提供给 Controller 调用的方法
    public List<Transaction> getAllTransactions() {
        // 这里就是普通的 Java 方法调用，调用 repository 的 findAll 方法
        return repository.findAll();
    }
}