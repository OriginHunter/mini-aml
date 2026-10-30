package com.miniaml.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniaml.mapper.AccountMapper;
import com.miniaml.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountService {
    public final AccountMapper accountMapper;

    public AccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public List<Account> getAllAccount() {
        log.info("开始查询所有帐户");
        return accountMapper.selectList(null);
    }

    public Account getAccountById(String id) {
        log.info("开始查询 ID 为{}帐户", id);
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getId, id);
        log.info("查询 ID 为{}的帐户成功",id);
        return accountMapper.selectOne(wrapper);
    }
    public Page<Account> getAccountPage(Integer pageNum, Integer pageSize, Long customerId) {
        // 【防御性编程】如果前端传的 pageSize 超过 100，强制赋值为 100
        if (pageSize > 100) {
            pageSize = 100;
        }

        Page<Account> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();

        // 【动态条件】如果传了 customerId，就加过滤条件
        wrapper.eq(customerId != null, Account::getCustomerId, customerId);

        // 【排序】按 ID 降序排列（或者升序，你喜欢就好）
        wrapper.orderByAsc(Account::getId);

        return accountMapper.selectPage(page, wrapper);
    }
}
