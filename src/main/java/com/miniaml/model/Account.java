package com.miniaml.model;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private String accountNo;
    private Long customerId;
    private BigDecimal balance;

    public Account(){}
    public Account(Long id, String accountNo, Long customerId, BigDecimal balance){
        this.id = id;
        this.accountNo = accountNo;
        this.customerId = customerId;
        this.balance = balance;
    }
    public Long getId(){
        return id;
    }
    public String getAccountNo(){
        return accountNo;
    }
    public Long getCustomerId(){
        return customerId;
    }
    public BigDecimal getBalance(){
        return balance;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setAccountNo(String accountNo){
        this.accountNo = accountNo;
    }
    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }
    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    @Override
    public String toString(){
        return "Account{" +
                "id=" + id +
                ", accountNo='"+ accountNo +
                "', customerId=" + customerId +
                ", balance=" + balance +
                "}";
    }
}
