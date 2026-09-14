# mini-aml

一个用于学习 Java 后端开发的最小反洗钱（AML）交易监测系统。

## 项目简介

模拟银行反洗钱系统的基本流程：

- 客户（Customer）拥有账户（Account）
- 账户上发生交易（Transaction）
- 规则（Rule）判断交易是否可疑
- 命中的结果会用于后续生成可疑案例（SuspiciousCase）

## 当前版本 v0.1

- 实现单笔大额交易规则（金额 ≥ 50000 命中）
- 控制台输出判断结果

## 技术栈

- Java 17
- Maven
- Git

## 项目结构

    src/main/java/com/miniaml/
    ├── Main.java                    程序入口
    ├── model/
    │   ├── Customer.java            客户
    │   ├── Account.java             账户
    │   └── Transaction.java         交易
    └── rule/
        ├── Rule.java                规则接口
        └── LargeAmountRule.java     单笔大额规则

## 运行方式

在 IDEA 中运行 `Main.java` 即可。

## 学习目标

- Java 面向对象基础
- 接口与多态
- BigDecimal 精确金额计算
- Git 版本控制