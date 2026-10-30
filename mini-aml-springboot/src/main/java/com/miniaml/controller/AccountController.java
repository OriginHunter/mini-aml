package com.miniaml.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniaml.common.Result;
import com.miniaml.model.Account;
import com.miniaml.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    public final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public Result<List<Account>> getAllAccount() {
        List<Account> list  = accountService.getAllAccount();
        return Result.success(list);
    }
    @GetMapping("/accounts/search")
    public Result<Account> getAccountById(@RequestParam String id) {
        Account list  = accountService.getAccountById(id);
        return Result.success(list);
    }
    @GetMapping("/accounts/page")
    public Result<Page<Account>> getAccountPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long customerId) { // 注意这里改成了 Long
        Page<Account> page = accountService.getAccountPage(pageNum, pageSize, customerId);
        return Result.success(page);
    }
}
