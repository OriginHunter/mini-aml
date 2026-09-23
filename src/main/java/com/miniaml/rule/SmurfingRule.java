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

public class SmurfingRule implements Rule {
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("50000");
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("40000");

    @Override
    public boolean hit(List<Transaction> transactions) {
        Map<LocalDate, List<Transaction>> byDate = groupByDate(transactions);
        List<LocalDate> dates = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Transaction>> entry : byDate.entrySet()) {
            dates.add(entry.getKey());
            amounts.add(sumTransactions(entry.getValue()));
        }
        for (int i = 0;i <= dates.size() - 3;i++) {
            LocalDate date1 = dates.get(i);
            LocalDate date2 = dates.get(i + 1);
            LocalDate date3 = dates.get(i + 2);

            long gap1 = ChronoUnit.DAYS.between(date1, date2);
            long gap2 = ChronoUnit.DAYS.between(date2, date3);

            if (gap1 == 1 && gap2 == 1) {
                BigDecimal amount1 = amounts.get(i);
                BigDecimal amount2 = amounts.get(i + 1);
                BigDecimal amount3 = amounts.get(i + 2);
                if(inRange(amount1) && inRange(amount2) && inRange(amount3)) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public String name() {
        return "拆分交易(连续3天每天4-5万)";
    }

    private boolean inRange(BigDecimal amount) {
        return (MAX_AMOUNT.compareTo(amount) > 0) && (MIN_AMOUNT.compareTo(amount) <= 0);
    }
    private static Map<LocalDate, List<Transaction>> groupByDate(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()));
    }
    private static BigDecimal sumTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add));
    }
}
