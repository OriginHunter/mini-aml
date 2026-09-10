package com.miniaml;
import com.miniaml.model.Account;
import com.miniaml.model.Customer;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args){
        Customer c = new Customer(1L, "张三", "110101199001011234");
        Account a = new Account(1L,"001",999L, new BigDecimal("9900"));

        System.out.println(c.getId());
        System.out.println(c.getName());
        System.out.println(c.getIdCard());

        System.out.println(a.getId());
        System.out.println(a.getAccountNo());
        System.out.println(a.getCustomerId());
        System.out.println(a.getBalance());

        c.setId(2L);
        c.setName("李四");
        c.setIdCard("110101199001015678");

        System.out.println(c);
    }
}
