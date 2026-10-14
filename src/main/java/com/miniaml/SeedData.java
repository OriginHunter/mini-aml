package com.miniaml;

import com.miniaml.dao.TransactionDao;
import com.miniaml.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeedData {
    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();
        LocalDateTime baseTime = LocalDateTime.now().minusDays(365);
        long currentId = 200000L;

        int total = 100000; // 先跑 10 条

        for (int i = 0; i < total; i++) {
            // 1. 生成 accountId（1~3 的随机数）
            long accountId = random.nextInt(1, 3+1);

            // 2. 生成 amount（1000 ~ 500000 的随机数）
            BigDecimal amount = BigDecimal.valueOf(random.nextInt(1000, 500000 + 1));

            // 3. 生成 transTime（baseTime 之后 365 天内的随机时间）
            LocalDateTime transTime = baseTime.plusSeconds(random.nextInt(365 * 24 * 60 * 60));

            // 4. 生成 type（"IN" 或 "OUT"）
            String type = random.nextBoolean() ? "IN" : "OUT";

            Transaction t = new Transaction(currentId++, accountId, amount, type, transTime);
            transactions.add(t);
        }

        TransactionDao dao = new TransactionDao();
        dao.insertBatch(transactions);
    }
}