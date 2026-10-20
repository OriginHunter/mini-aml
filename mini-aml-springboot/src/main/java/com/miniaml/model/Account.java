package com.miniaml.model;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private String accountNo;
    private Long customerId;
    private BigDecimal balance;

    public Account() {
    }

    public Account(Long id, String accountNo, Long customerId, BigDecimal balance) {
        this.id = id;
        this.accountNo = accountNo;
        this.customerId = customerId;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNo='" + accountNo +
                "', customerId=" + customerId +
                ", balance=" + balance +
                "}";
    }
}
/*
Account a = new Account(1L,"622200001",1L, new BigDecimal("100000.00"));

        System.out.println(a.getId());
        System.out.println(a.getAccountNo());
        System.out.println(a.getCustomerId());
        System.out.println(a.getBalance());

        a.setAccountNo("622200001");
        a.setCustomerId(3L);
        a.setBalance(new BigDecimal("200000.00"));

        System.out.println(a);
*/
