package com.miniaml.controller;

import com.miniaml.common.Result;
import com.miniaml.model.Customer;
import com.miniaml.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public Result<List<Customer>> getCustomer() {
        List<Customer> list = customerService.getCustomer();

        return Result.success(list);
    }

    @GetMapping("/customers/search")
    public Result<List<Customer>> getCustomerByName(@RequestParam String name) {
        List<Customer> list = customerService.getCustomerbyName(name);
        return Result.success(list);
    }

}
