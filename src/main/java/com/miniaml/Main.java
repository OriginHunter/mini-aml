package com.miniaml;

import com.miniaml.exception.InvalidTransactionException;
import com.miniaml.learning.LambdaExamples;
import com.miniaml.learning.StreamExamples;
import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;
import com.miniaml.rule.DailyAmountRule;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;
import com.miniaml.rule.SmurfingRule;
import com.miniaml.util.ListUtil;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //创建客户
        Customer customer = new Customer(
                1L,
                "张三",
                "110101199001011234");
        //创建账户
        Account account = new Account(
                1L,
                "622200001",
                customer.getId(),
                new BigDecimal("100000.00"));
        //random生成交易
        createTransactions();
        try {
            //创建交易列表
            List<Transaction> transactions = loadTransactions();
            //打印标题
            printHeader();
            //打印客户与账户
            printCustomerInfo(customer, account);
            //打印交易
            printTransactions(transactions);
            //规则列表
            List<Rule<List<Transaction>>> rules = createRules();
            //按照账户给交易分组
            Map<Long, List<Transaction>> byAccount = groupByAccount(transactions);
            //判断规则是否命中
            checkRulesByAccount(byAccount, rules);
            //把按照账户分组的交易按日期分组
            Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate = groupByAccountDate(byAccount);
            byAccountDate.forEach((id, t) -> System.out.println("账户" + id + ":" + t));
            //把每个账户每天交易金额求和
            sumByAccountDate(byAccountDate);
            //
            testSmurfingTransaction();
        } catch (InvalidTransactionException e) {
            System.out.println("数据错误：" + e.getMessage());
        }
    }

    private static void testSmurfingTransaction() {
        List<Transaction> transactions1 = new ArrayList<>();
        List<Transaction> transactions2 = new ArrayList<>();
        List<Transaction> transactions3 = new ArrayList<>();
        List<Transaction> transactions4 = new ArrayList<>();
        transactions1.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 1, 10,30)));
        transactions1.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 2, 14,0)));
        transactions1.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 3, 16,0)));
        transactions2.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 7, 10,30)));
        transactions2.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 8, 14,0)));
        transactions2.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 10, 16,0)));
        transactions3.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 11, 10,30)));
        transactions3.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("40000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 12, 14,0)));
        transactions3.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("50000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 13, 16,0)));
        transactions4.add(new Transaction(
                10001L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 11, 10,30)));
        transactions4.add(new Transaction(
                10002L,
                1L,
                new BigDecimal("30000.00"),
                "OUT",
                LocalDateTime.of(2026, 9, 12, 14,0)));
        transactions4.add(new Transaction(
                10003L,
                1L,
                new BigDecimal("40000.00"),
                "IN",
                LocalDateTime.of(2026, 9, 13, 16,0)));        //规则列表
        List<Rule<List<Transaction>>> rules = createRules();
        //按照账户给交易分组
        Map<Long, List<Transaction>> byAccount1 = groupByAccount(transactions1);
        Map<Long, List<Transaction>> byAccount2 = groupByAccount(transactions2);
        Map<Long, List<Transaction>> byAccount3 = groupByAccount(transactions3);
        Map<Long, List<Transaction>> byAccount4 = groupByAccount(transactions4);
        //判断规则是否命中
        System.out.println("===== 场景 1：连续 3 天 4 万 =====");
        checkRulesByAccount(byAccount1, rules);

        System.out.println("===== 场景 2：不连续 =====");
        checkRulesByAccount(byAccount2, rules);

        System.out.println("===== 场景 3：有一天 5 万 =====");
        checkRulesByAccount(byAccount3, rules);

        System.out.println("===== 场景 4：有一天 3 万 =====");
        checkRulesByAccount(byAccount4, rules);
    }

    private static void sumByAccountDate(Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate) {
        for (Map.Entry<Long, Map<LocalDate, List<Transaction>>> byAccountDateEntry : byAccountDate.entrySet()) {
            System.out.println(" id :" + byAccountDateEntry.getKey());
            for (Map.Entry<LocalDate, List<Transaction>> byDateEntry : byAccountDateEntry.getValue().entrySet()) {

                System.out.println(" 日期:" + byDateEntry.getKey() + "金额" + sumTransactions(byDateEntry.getValue()));
            }
        }
    }

    private static BigDecimal sumTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add));
    }

    private static Map<Long, Map<LocalDate, List<Transaction>>> groupByAccountDate(Map<Long, List<Transaction>> byAccount) {
        Map<Long, Map<LocalDate, List<Transaction>>> byAccountDate = new HashMap<Long, Map<LocalDate, List<Transaction>>>();
        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            byAccountDate.put(entry.getKey(), groupByDate(entry.getValue()));
        }
        return byAccountDate;
    }

    private static Map<LocalDate, List<Transaction>> groupByDate(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()));
    }

    private static void testLambda() {
        List<Transaction> transactions = loadTransactions();
        System.out.println("---Lambda测试---");
        LambdaExamples.forEachExample(transactions);
        LambdaExamples.computeIfAbsentExample(transactions);
        LambdaExamples.methodReferenceExample(transactions);
        LambdaExamples.removeIfExample(transactions);
        LambdaExamples.sortExample(transactions);
    }

    private static void testListUtil() {
        List<Transaction> transactions = loadTransactions();
        System.out.println("---ListUtil测试---");
        System.out.println("transactions空不空:" + ListUtil.isEmpty(transactions));
        System.out.println("null空不空:" + ListUtil.isEmpty(null));

        System.out.println("第一笔交易的ID:" + (!transactions.isEmpty() ? ListUtil.getFirst(transactions).getId() : "没有"));

        System.out.println("所有ID:" + ListUtil.map(transactions, t -> t.getId()));
        System.out.println("所有金额:" + ListUtil.map(transactions, t -> t.getAmount()));

    }

    private static void testStreamExamples() {
        List<Transaction> transactions = loadTransactions();

        System.out.println("--- filter ---");
        StreamExamples.filterExample(transactions);

        System.out.println("--- map ---");
        StreamExamples.mapExample(transactions);

        System.out.println("--- groupBy ---");
        StreamExamples.groupByExample(transactions);

        System.out.println("--- count ---");
        StreamExamples.countExample(transactions);

        System.out.println("--- max ---");
        StreamExamples.maxExample(transactions);

        System.out.println("--- reduce ---");
        StreamExamples.reduceExample(transactions);
    }

    private static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"))) {
            reader.readLine();
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] string = line.split(",");
                if (string.length != 5) {
                    throw new InvalidTransactionException(
                            "第 " + lineNum + " 行字段个数不对：" + line);
                }
                try {
                    Long id = Long.parseLong(string[0]);
                    Long accountId = Long.parseLong(string[1]);
                    BigDecimal amount = new BigDecimal(string[2]);
                    String type = string[3];
                    LocalDateTime transTime = LocalDateTime.parse(string[4]);
                    if (!"IN".equals(type) && !"OUT".equals(type)) {
                        throw new InvalidTransactionException(
                                "第 " + lineNum + " 行交易类型非法：" + type);
                    }

                    Transaction t = new Transaction(id, accountId, amount, type, transTime);
                    transactions.add(t);
                } catch (NumberFormatException | DateTimeParseException e) {
                    throw new InvalidTransactionException(
                            "第 " + lineNum + " 行数据格式不对：" + line);
                }

            }
        } catch (IOException e) {
            System.out.println("读文件失败：" + e.getMessage());
        }
        return transactions;
    }

    private static void createTransactions() {
        Random random = new Random(2);
        LocalDateTime start = LocalDateTime.of(2026, 9, 1, 0, 0);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv"))) {
            writer.write("id,accountId,amount,type,transTime");
            writer.newLine();
            for (int i = 0; i < 10; i++) {
                int id = 10001 + i;

                int accountId = random.nextInt(3) + 1;

                boolean isIn = random.nextBoolean();
                String type = isIn ? "IN" : "OUT";

                int amount = random.nextInt(220000 - 1000 + 1) + 1000;

                int dayOfMonth = random.nextInt(30);
                int hour = random.nextInt(24);
                LocalDateTime time = start.plusHours(hour).plusDays(dayOfMonth);

                writer.write(
                        id + "," +
                                accountId + "," +
                                amount + ".00," +
                                type + "," +
                                time);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("错误：" + e.getMessage());
        }
    }

    private static List<Rule<List<Transaction>>> createRules() {
        List<Rule<List<Transaction>>> rules = new ArrayList<>();
        rules.add(new LargeAmountRule());
        rules.add(new DailyAmountRule());
        rules.add(new SmurfingRule());
        return rules;
    }

    private static void printHeader() {
        System.out.println("运行时间：" + LocalDateTime.now());
        System.out.println("""
                ========================
                      mini-aml
                   交易监测系统 v0.1
                ========================
                """);
    }

    private static void printCustomerInfo(Customer customer, Account account) {
        System.out.println(
                "客户名:" + customer.getName() + "\n" +
                        "账户ID:" + account.getAccountNo() + "\n");
    }

    private static void printTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(
                    "交易流水号:" + transaction.getId() + "\n" +
                            "交易账户:" + transaction.getAccountId() + "\n" +
                            "金额:" + transaction.getAmount() + "\n" +
                            "交易时间:" + transaction.getTransTime() + "\n");
        }
    }

    private static Map<Long, List<Transaction>> groupByAccount(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));
    }

    private static void checkRulesByAccount(Map<Long, List<Transaction>> byAccount, List<Rule<List<Transaction>>> rules) {
        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            System.out.println("========== 账户 " + entry.getKey() + " ==========");
            System.out.println("该账户共有 " + entry.getValue().size() + " 笔交易");
            for (Rule<List<Transaction>> rule : rules) {
                if (rule.hit(entry.getValue())) {
                    System.out.println("规则:" + rule.name() + " ⚠ 命中");
                } else {
                    System.out.println("规则:" + rule.name() + " 未命中");
                }
            }
        }
    }
}
