package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class DailyAmountRule implements Rule {
    private static final BigDecimal DAILY_THRESHOLD  = new BigDecimal("200000.00");
    @Override
    public boolean hit(List<Transaction> transactions) {
        BigDecimal total = BigDecimal.ZERO ;
        for (Transaction transaction : transactions) {
            total =total.add(transaction.getAmount());
        }
        return total.compareTo(DAILY_THRESHOLD ) >= 0;
    }

    @Override
    public String name() {
        return "单日累计大于等于 20 万";
    }
}
