package com.miniaml.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Customer {
    private Long id;
    private String name;
    private String idCard;
    private LocalDateTime createTime;
    public Customer() {
    }

    public Customer(Long id, String name, String idCard,LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdCard() {
        return idCard;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name +
                "', idCard='" + idCard +
                "', creatTime='" + createTime +
                "'}";
    }

}
/*
        Customer c = new Customer(1L, "张三", "110101199001011234");

        System.out.println(c.getId());
        System.out.println(c.getName());
        System.out.println(c.getIdCard());

        c.setId(2L);
        c.setName("李四");
        c.setIdCard("110101199001015678");

        System.out.println(c);        a.setId(2L);
*/
