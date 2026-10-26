package com.miniaml.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miniaml.mapper.CustomerMapper;
import com.miniaml.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // 使用 Lombok 自动生成构造器
public class CustomerService {

    // 只注入 Mapper，再也不需要 Repository 了
    private final CustomerMapper customerMapper;

    /**
     * 1. 查询所有客户
     * MP 自带 selectList(null) 表示查询全表
     */
    public List<Customer> getCustomer() {
        log.info("开始查询所有客户");
        return customerMapper.selectList(null);
    }

    /**
     * 2. 根据名字查询（使用条件构造器 Wrapper）
     */
        public List<Customer> getCustomerByName(String name) {
            log.info("按名字查询客户：{}", name);
            // 创建 Lambda 条件构造器
            LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
            // eq 等于：数据库的 name 字段 等于 传入的 name
            wrapper.eq(Customer::getName, name);
            // 执行查询
            return customerMapper.selectList(wrapper);
        }

    /**
     * 3. 新增客户
     * MP 自带 insert 方法，自动处理自增主键回填
     */
    public void addCustomer(Customer customer) {
        log.info("准备新增客户：{}", customer.getName());
        // 注意：MP 会自动识别 @TableId(type = IdType.AUTO)，插入时忽略 ID
        customerMapper.insert(customer);
    }

    /**
     * 4. 更新客户
     * MP 自带 updateById，根据主键 ID 更新非空字段
     */
    public void updateCustomer(Customer customer) {
        log.info("准备更新客户，ID：{}", customer.getId());
        customerMapper.updateById(customer);
    }

    /**
     * 5. 删除客户
     * MP 自带 deleteById
     */
    public void deleteCustomer(Long id) {
        log.info("准备删除客户，ID：{}", id);
        customerMapper.deleteById(id);
    }
}