package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class LargeAmountRule implements Rule<List<Transaction>> {

    private static final BigDecimal THRESHOLD = new BigDecimal("50000");

    @Override
    public boolean hit(List<Transaction> transactions) {
        return !findHits(transactions).isEmpty();
    }

    @Override
    public List<Transaction> findHits(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount().compareTo(THRESHOLD) >= 0)
                .collect(Collectors.toList());
    }

    @Override
    public String name() {
        return "单笔金额大于等于 5 万";
    }
}