package com.miniaml.learning;

import com.miniaml.model.Transaction;

import java.math.BigDecimal;
import java.util.*;

public class LambdaExamples {
    //forEach 输出 Lambda及其简化 forEachExample
    public static void forEachExample(List<Transaction> transactions) {
        transactions.forEach(t -> System.out.println(t));
        transactions.forEach(System.out::println);
    }

    //forEach 输出ID 与toList methodReferenceExample
    public static void methodReferenceExample(List<Transaction> transactions) {
        List<Long> ids = transactions.stream()
                .map(Transaction::getId)
                .toList();
        System.out.println(ids);
    }

    //removeIf  输出前后 removeIfExample
    public static void removeIfExample(List<Transaction> transactions) {
        List<Transaction> copyT = new ArrayList<>(transactions);
        copyT.forEach(t -> System.out.println(t));
        copyT.removeIf(t -> t.getAmount().compareTo(BigDecimal.valueOf(60000L)) < 0);
        copyT.forEach(t -> System.out.println(t));
    }

    //computeIfAbsent 按照账户add computeIfAbsentExample
    public static void computeIfAbsentExample(List<Transaction> transactions) {
        Map<Long, List<Transaction>> byAccount = new HashMap<>();
        for (Transaction t : transactions) {
            byAccount.computeIfAbsent(t.getAccountId(), k -> new ArrayList<>()).add(t);
        }
        byAccount.forEach((k, v) -> System.out.println("账户" + k + "有 " + v.size() + " 笔交易"));
    }

    //sort 从小到大，ID:金额 sortExample
    public static void sortExample(List<Transaction> transactions) {
        List<Transaction> copyT = new ArrayList<>(transactions);
        copyT.sort(Comparator.comparing(Transaction::getAmount));
        copyT.forEach(t -> System.out.println(t.getId() + " : " + t.getAmount()));
    }
}
