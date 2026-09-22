package com.miniaml;

import com.miniaml.exception.InvalidTransactionException;
import com.miniaml.learning.StreamExamples;
import com.miniaml.model.Account;
import com.miniaml.model.Customer;
import com.miniaml.model.Transaction;
import com.miniaml.rule.DailyAmountRule;
import com.miniaml.rule.LargeAmountRule;
import com.miniaml.rule.Rule;
import com.miniaml.util.ListUtil;

import java.io.*;
import java.math.BigDecimal;
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
            List<Rule> rules = createRules();
            //按照账户给交易分组
            Map<Long, List<Transaction>> byAccount = groupByAccount(transactions);
            //判断规则是否命中
            checkRulesByAccount(byAccount, rules);
            //Stream测试
            testStreamExamples();
            //泛型测试
            testListUtil();
        } catch (InvalidTransactionException e) {
            System.out.println("数据错误：" + e.getMessage());
        }
    }
    private static void testListUtil() {
        List<Transaction> transactions = loadTransactions();
        System.out.println("---ListUtil测试---");
        System.out.println("transactions空不空:" + ListUtil.isEmpty(transactions));
        System.out.println("null空不空:" + ListUtil.isEmpty(null));

        System.out.println("第一笔交易的ID:" + (!transactions.isEmpty() ? ListUtil.getFirst(transactions).getId() : "没有"));

        System.out.println("所有ID:" + ListUtil.map(transactions,t -> t.getId()));
        System.out.println("所有金额:" + ListUtil.map(transactions,t -> t.getAmount()));

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

    private static List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new LargeAmountRule());
        rules.add(new DailyAmountRule());
        return rules;
    }

    private static void printHeader() {
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

    private static void checkRulesByAccount(Map<Long, List<Transaction>> byAccount, List<Rule> rules) {
        for (Map.Entry<Long, List<Transaction>> entry : byAccount.entrySet()) {
            System.out.println("========== 账户 " + entry.getKey() + " ==========");
            System.out.println("该账户共有 " + entry.getValue().size() + " 笔交易");
            for (Rule rule : rules) {
                if (rule.hit(entry.getValue())) {
                    System.out.println("规则:" + rule.name() + " ⚠ 命中");
                } else {
                    System.out.println("规则:" + rule.name() + " 未命中");
                }
            }
        }
    }
}
