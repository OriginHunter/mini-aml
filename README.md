# mini-aml

一个用于学习 Java 后端开发的最小反洗钱（AML）交易监测系统。

## 项目简介
（保持）

## 当前版本 v0.2

相比 v0.1，新增：

- 3 条 AML 规则：
  - 单笔大额交易（≥ 5 万）
  - 单日累计（≥ 20 万）
  - 拆分交易 Smurfing（连续 3 天每天 4~5 万）
- 按账户 + 日期三层分组统计
- CSV 文件读写（交易数据从文件加载）
- 脏数据检测 + 自定义异常
- 泛型规则引擎（Rule<T>）
- 单元测试覆盖率 88%

## 技术栈
（保持：Java 17 / Maven / Git）
再补充：
- JUnit 5 测试
- JaCoCo 覆盖率

## 项目结构
（更新，加入 learning / util / exception 包）

    src/main/java/com/miniaml/
    ├── Main.java                    程序入口
    ├── model/                       数据类
    │   ├── Customer.java
    │   ├── Account.java
    │   ├── Transaction.java
    │   └── SuspiciousCase.java
    ├── rule/                        规则
    │   ├── Rule.java                泛型规则接口
    │   ├── LargeAmountRule.java
    │   ├── DailyAmountRule.java
    │   └── SmurfingRule.java
    ├── util/
    │   └── ListUtil.java            泛型工具类
    ├── exception/
    │   └── InvalidTransactionException.java
    └── learning/                    学习例程库
        ├── StreamExamples.java
        └── LambdaExamples.java

## 如何运行
（保持）

## 运行测试

```bash
mvn test