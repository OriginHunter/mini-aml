package com.miniaml;
import com.miniaml.model.Customer;
public class Main {
    public static void main(String[] args){
        Customer c = new Customer(1L, "张三", "110101199001011234");
        System.out.println(c.getId());
        System.out.println(c.getName());
        System.out.println(c.getIdCard());

        c.setId(2L);
        c.setName("李四");
        c.setIdCard("110101199001015678");

        System.out.println(c);
    }
}
