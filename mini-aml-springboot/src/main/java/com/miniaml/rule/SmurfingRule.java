package com.miniaml.rule;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SmurfingRule implements Rule<List<Transaction>> {

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("40000");
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("50000");

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

        List<LocalDate> dates = new ArrayList<>(byDate.keySet());
        List<Transaction> hits = new ArrayList<>();

        for (int i = 0; i <= dates.size() - 3; i++) {
            LocalDate d1 = dates.get(i);
            LocalDate d2 = dates.get(i + 1);
            LocalDate d3 = dates.get(i + 2);

            long gap1 = ChronoUnit.DAYS.between(d1, d2);
            long gap2 = ChronoUnit.DAYS.between(d2, d3);

            if (gap1 == 1 && gap2 == 1) {
                BigDecimal total1 = sumOf(byDate.get(d1));
                BigDecimal total2 = sumOf(byDate.get(d2));
                BigDecimal total3 = sumOf(byDate.get(d3));

                if (inRange(total1) && inRange(total2) && inRange(total3)) {
                    hits.addAll(byDate.get(d1));
                    hits.addAll(byDate.get(d2));
                    hits.addAll(byDate.get(d3));
                }
            }
        }
        return hits;
    }

    private BigDecimal sumOf(List<Transaction> transactions) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            total = total.add(t.getAmount());
        }
        return total;
    }

    private boolean inRange(BigDecimal amount) {
        return amount.compareTo(MIN_AMOUNT) >= 0
                && amount.compareTo(MAX_AMOUNT) < 0;
    }

    @Override
    public String name() {
        return "拆分交易（连续3天每天4-5万）";
    }
}