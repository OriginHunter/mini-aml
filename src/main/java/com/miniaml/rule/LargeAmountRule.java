package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;

public class LargeAmountRule implements Rule {
    private static final BigDecimal THRESHOLD = new BigDecimal("50000.00");
    @Override
    public boolean hit(Transaction transaction){
        return transaction.getAmount().compareTo(THRESHOLD) >= 0;
    }
    @Override
    public String name(){
        return "单笔金额大于等于 5 万";
    }
}
