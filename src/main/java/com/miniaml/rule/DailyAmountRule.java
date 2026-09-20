package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DailyAmountRule implements Rule {
    private static final BigDecimal DAILY_THRESHOLD  = new BigDecimal("200000.00");
    @Override
    public boolean hit(List<Transaction> transactions) {
        Map<LocalDate, List<Transaction>> byDate= transactions.stream()
                .collect(Collectors.groupingBy(transaction ->
                        transaction.getTransTime().toLocalDate()));
        for (Map.Entry<LocalDate, List<Transaction>> entry : byDate.entrySet()) {
            BigDecimal total = BigDecimal.ZERO ;
            for (Transaction transaction : entry.getValue()) {
                    total = total.add(transaction.getAmount());
            }
            if (total.compareTo(DAILY_THRESHOLD) >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String name() {
        return "单日累计大于等于 20 万";
    }
}
