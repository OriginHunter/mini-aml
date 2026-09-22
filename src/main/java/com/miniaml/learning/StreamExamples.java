package com.miniaml.learning;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamExamples {
    public static void filterExample(List<Transaction> transactions) {
        List<Transaction> bigOnes = transactions.stream()
                .filter(transaction ->
                        transaction.getAmount().compareTo(new BigDecimal("50000")) >= 0  )
                .collect(Collectors.toList());
        System.out.println("大额交易：" + bigOnes.size() + " 笔");
    }
    public static void mapExample(List<Transaction> transactions) {
        List<Long> ids = transactions.stream()
                .map(Transaction::getId)
                .collect(Collectors.toList());
        System.out.println("所有 id：" + ids);
    }
    public static void groupByExample(List<Transaction> transactions) {
        Map<String, List<Transaction>> byType = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getType));
        byType.forEach((type, t) -> System.out.println("类型" + type + " 数量:"+ t.size()));
    }
    public static void countExample(List<Transaction> transactions) {
        long count = transactions.stream()
                .count();
        System.out.println("总数" + count);
    }
    public static void maxExample(List<Transaction> transactions) {
        Optional<Transaction> max = transactions.stream()
                .max(Comparator.comparing(Transaction::getAmount));
        max.ifPresent(transaction -> System.out.println(transaction));
    }
    public static void reduceExample(List<Transaction> transactions) {
        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("总计" + total);
    }
}
