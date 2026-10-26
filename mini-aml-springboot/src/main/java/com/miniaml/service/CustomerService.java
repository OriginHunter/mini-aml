package com.miniaml.service;

import com.miniaml.model.Customer;
import com.miniaml.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository =customerRepository;
    }
    public List<Customer> getCustomer() {
        log.info("开始查询客户");
        List<Customer> customers = customerRepository.findAll();
        log.info("成功查询");
        return customers;
    }
    public List<Customer> getCustomerbyName(String name) {
        log.info("开始查询客户{}", name);
        List<Customer> customers = customerRepository.getByName(name);
        log.info("成功查询");
        return customers;
    }

    /**
     * 更新客户信息
     */
    public void updateCustomer(Customer customer) {
        log.info("准备更新客户，ID：{}", customer.getId());
        int rows = customerRepository.update(customer);
        if (rows == 0) {
            // 抛出异常，会被全局异常处理器拦截，返回友好的提示
            throw new RuntimeException("更新失败，该客户不存在");
        }
    }

    /**
     * 删除客户
     */
    public void deleteCustomer(Long id) {
        log.info("准备删除客户，ID：{}", id);
        int rows = customerRepository.deleteById(id);
        if (rows == 0) {
            throw new RuntimeException("删除失败，该客户不存在");
        }
    }
    /**
     * 新增客户
     */
    public void addCustomer(Customer customer) {
        log.info("准备新增客户，姓名：{}", customer.getName());
        int rows = customerRepository.insert(customer);
        if (rows == 0) {
            throw new RuntimeException("新增客户失败");
        }
    }
}
