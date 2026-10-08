package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DailyAmountRule implements Rule<List<Transaction>> {

    private static final BigDecimal DAILY_THRESHOLD = new BigDecimal("200000.00");

    @Override
    public boolean hit(List<Transaction> transactions) {
        return !findHits(transactions).isEmpty();
    }

    @Override
    public List<Transaction> findHits(List<Transaction> transactions) {
        Map<LocalDate, List<Transaction>> byDate = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()));

        List<Transaction> hits = new ArrayList<>();
        for (List<Transaction> dayTxns : byDate.values()) {
            BigDecimal total = BigDecimal.ZERO;
            for (Transaction t : dayTxns) {
                total = total.add(t.getAmount());
            }
            if (total.compareTo(DAILY_THRESHOLD) >= 0) {
                hits.addAll(dayTxns);
            }
        }
        return hits;
    }

    @Override
    public String name() {
        return "单日累计大于等于 20 万";
    }
}