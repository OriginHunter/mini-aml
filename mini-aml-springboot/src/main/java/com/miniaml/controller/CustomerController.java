package com.miniaml.controller;

import com.miniaml.common.Result;
import com.miniaml.model.Customer;
import com.miniaml.service.CustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
        List<Customer> list = customerService.getCustomerByName(name);
        return Result.success(list);
    }

    /**
     * 更新客户信息（全量更新）
     * URL 里的 {id} 会被 @PathVariable 捕获
     */
    @PutMapping("/customers/{id}")
    public Result<String> updateCustomer(@PathVariable Long id, @Validated @RequestBody Customer customer) {
        // 【安全防御】强制用 URL 里的真实 id 覆盖前端 JSON 里可能乱传的 id
        customer.setId(id);
        customerService.updateCustomer(customer);
        return Result.success("更新成功");
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/customers/{id}")
    public Result<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.success("删除成功");
    }
    /**
     * 新增客户
     * @param customer 前端传来的 JSON 数据
     * @return 统一返回体
     */
    @PostMapping("/customers")
    public Result<String> addCustomer(@Validated @RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return Result.success("新增成功");
    }
    @PutMapping("/customers/restore/{id}")
    public Result<String> restoreCustomer(@PathVariable Long id) {
        customerService.restoreCustomer(id);
        return Result.success("客户恢复成功");
    }
}
