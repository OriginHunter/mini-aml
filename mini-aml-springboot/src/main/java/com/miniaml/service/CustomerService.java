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
}
