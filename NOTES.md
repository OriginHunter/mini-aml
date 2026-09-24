# mini-aml 学习笔记

---

## 🎮 Java Backend RPG 快照

**更新日期**：2026-09-24

| 项目 | 内容 |
|---|---|
| 玩家 | OriginHunter |
| 等级 | Lv.12 Java 后端工程师（初级） |
| XP | 4680 / 5000 |
| 金币 | 约 800 |
| 当前关卡 | 第 2 章收尾 |

### 主线进度

```
第1章：Java 基础与 OOP           ✅ 100%
第2章：集合与进阶                 ✅ 95%
  ├─ 2-1 List                     ✅
  ├─ 2-2 Map + 异常 + 重构         ✅
  ├─ 2-3 Set + fail-fast          ✅
  ├─ 2-4 JUnit 5                  ✅
  ├─ 2-5 Lambda                   ✅
  ├─ 2-6 Stream API               ✅
  ├─ 2-7 泛型深入                 ✅
  └─ 2-8 工程化（覆盖率/泛型）      ✅
第3章：MySQL + JDBC              ⬜ 0%
第4章：Spring Boot               ⬜ 0%
第5章：MyBatis + REST            ⬜ 0%
第6章：Redis / Docker / Linux     ⬜ 0%
第7章：Vue                       ⬜ 0%
```

### 已解锁成就（25）

```
🏆 Java 启蒙           — 第一次运行 Java 程序
🏆 OOP 觉醒            — 掌握封装、继承、多态、接口
🏆 BigDecimal 守护者    — 掌握金额精确计算
🏆 Git 入门            — 完成第一次 git commit
🏆 GitHub 上场         — 代码推送到远程仓库
🏆 List 探索者         — 掌握 List 的增删改查
🏆 Map 探索者          — 独立使用 Map<Long, List<Transaction>>
🏆 异常猎人            — 成功捕获并处理异常
🏆 代码重构者          — 把 80 行 main 拆成 8 个方法
🏆 Set 收藏家          — 掌握 Set 去重与遍历
🏆 事实核查者          — 实测发现说明中的不准确之处
🏆 规范质疑者          — 质疑"过度规范"，只做必要的
🏆 单元测试员          — 完成第一组 JUnit 测试
🏆 测试覆盖者          — 3 个类，10 个测试全通过
🏆 Lambda 初学者       — 掌握 forEach / removeIf / computeIfAbsent
🏆 Stream 炼金术士      — 掌握 filter / map / collect / groupingBy
🏆 Stream 入门者       — 掌握 stream / filter / map
🏆 观察者              — 发现"原数据不可变"的重要特性
🏆 文件 IO 入门者      — 掌握 FileReader / FileWriter
🏆 数据管道工程师      — 让数据从生成 → 文件 → 业务流通
🏆 Bug 猎人            — 发现并修复"变量作用域"bug
🏆 规则工程师          — DailyAmountRule 真正实现按日期分组
🏆 反洗钱规则师        — 掌握 Smurfing 识别（模式型规则）
🏆 业务建模师          — 理解阈值型/统计型/模式型三种规则
🏆 窗口侦探            — 掌握滑动窗口算法
🏆 规则验证师          — 用多场景测试验证规则正确性
🏆 泛型规则引擎师      — 把规则接口参数化，支持任意类型
🏆 覆盖率猎人          — 单测覆盖率从 8% 提升到 88%
🏆 边界测试师          — 补全所有规则的边界场景测试
🏆 命名规范者          — 测试代码全部英文命名
```

### 项目进度

```
mini-aml

v0.1 ██████████ 100%   控制台版
v0.2 █████████░ 95%    集合 + 异常 + 分组 + 测试 + CSV + Stream + 泛型规则
v0.3 ⬜                接入 MySQL
v0.4 ⬜                Spring Boot API
v0.5 ⬜                MyBatis + REST
v1.0 ⬜                完整系统
```

---

## 📚 知识清单

### Java 基础

- `package` / `import`
- 类 / 对象 / 字段 / 方法 / 构造方法
- `private` / `public` / `protected` / 默认
- `static` / `final` / 常量
- getter / setter / `toString()`
- `this` 关键字
- `interface` / `implements`
- 多态

### 常用类型

| 类型 | 用途 | 注意 |
|---|---|---|
| `Long` / `Integer` | 整数（对象版） | 比较用 equals |
| `String` | 字符串 | 比较用 equals |
| `BigDecimal` | 金额 | 用字符串构造，用 compareTo 比较 |
| `LocalDateTime` | 日期时间 | ISO 格式 `2026-09-24T14:00` |
| `LocalDate` | 仅日期 | `2026-09-24` |
| `Optional<T>` | 可能没值 | ifPresent / orElse / orElseThrow |

### 集合

- `List`（有序可重复）/ `ArrayList`
- `Set`（无序不重复）/ `HashSet`
- `Map`（键值对）/ `HashMap` / `TreeMap`
- `TreeMap`：按 key 自动排序
- 自动装箱 / 拆箱
- `containsKey` vs `get`

### HashMap 原理（面试重点）

```
底层结构：数组 + 链表 + 红黑树
默认容量：16
put 过程：
  ① hash 扰动：h ^ (h >>> 16)
  ② 定位：hash & (容量 - 1)
  ③ 桶空 → 直接放；桶非空 → 遍历链表/树
     key 相同 → 覆盖；key 不同 → 挂到链表尾部
树化条件：链表长度 ≥ 8 且容量 ≥ 64
退化条件：树节点数 ≤ 6
扩容条件：元素数 > 容量 × 0.75
扩容方式：容量翻倍，用同一个 hash 重算下标
  因为容量是 2 的幂，新下标要么不变，要么是原位置 + 旧容量
```

### 异常

- `try-catch-finally`
- `throw` 抛异常 / `throws` 声明
- checked（`IOException`）vs unchecked（`IllegalArgumentException`）
- `try-with-resources`
- 自定义异常：继承 `RuntimeException`，提供两个构造方法
- `throw` = 上报问题；`catch` = 处理问题

### Lambda & Stream

**Lambda 心法**：`参数 -> 表达式`

| Lambda | 用在哪 |
|---|---|
| `x -> System.out.println(x)` | `forEach` |
| `(k, v) -> ...` | `Map.forEach` |
| `x -> x < 0` | `removeIf` / `filter` |
| `k -> new ArrayList<>()` | `computeIfAbsent` |

**Stream 三段结构**：

```
集合.stream()          ← 创建流
    .filter(...)        ← 中间操作
    .map(...)           ← 中间操作
    .collect(...)       ← 终止操作
```

**常用操作**：

| 操作 | 作用 |
|---|---|
| `filter(Predicate)` | 过滤 |
| `map(Function)` | 转换 |
| `collect(Collectors.toList())` | 收集成 List |
| `collect(Collectors.groupingBy(...))` | 分组 |
| `collect(Collectors.reducing(...))` | 聚合求和 |
| `forEach(Consumer)` | 遍历 |
| `count()` | 计数 |
| `max()` / `min()` | 最大 / 最小 |
| `reduce()` | 聚合 |
| `sorted()` / `distinct()` / `limit(n)` | 排序 / 去重 / 取前 n |

**重要特性**：Stream 不改原集合，只输出新结果。

### 泛型

**泛型方法**：

```java
public static <T> T method(T param) { ... }
public static <T, R> List<R> map(List<T> list, Function<T, R> fn) { ... }
```

**泛型类**：

```java
public class Box<T> {
    private T value;
    public T getValue() { return value; }
}
```

**泛型接口**：

```java
public interface Rule<T> {
    boolean hit(T target);
    String name();
}

public class LargeAmountRule implements Rule<List<Transaction>> { ... }
```

**使用泛型的意义**：

- 类型安全（编译期检查）
- 代码复用（一个方法支持多种类型）
- 消除强制类型转换

### 文件 IO

**写文件**：

```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter("路径"))) {
    writer.write("内容");
    writer.newLine();
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

**读文件**：

```java
try (BufferedReader reader = new BufferedReader(new FileReader("路径"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        // 处理 line
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

**CSV 处理**：

- `String.split(",")` 切分
- `Long.parseLong(s)` / `new BigDecimal(s)` / `LocalDateTime.parse(s)` 类型转换

**Random**：

- `new Random()`：每次不同
- `new Random(种子)`：固定种子 → 可复现
- `nextInt(n)`：0~n-1
- `new Random(2)` 这种固定种子的用法，用于**测试数据可复现**

### 日期时间

- `ChronoUnit.DAYS.between(d1, d2)`：算两日期差几天
- `LocalDateTime.toLocalDate()`：取日期部分
- `LocalDate.plusDays(n)`：加 n 天
- `date.atTime(时, 分)`：日期变日期时间

### 测试 JUnit 5

```java
@Test
void methodName_scenario_expectedResult() {
    // Arrange 准备
    // Act 执行
    // Assert 断言
}
```

**常用断言**：

- `assertTrue` / `assertFalse`
- `assertEquals`
- `assertNull` / `assertNotNull`
- `assertThrows`
- `assertDoesNotThrow`

**命名规范**：

```
方法名_场景_期望结果
例：hit_amount50000_returnsTrue
例：isEmpty_null_returnsTrue
```

**测试类放**：`src/test/java`，和被测类同包名

### JaCoCo 覆盖率

**pom.xml 配置**：

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
            <configuration>
                <excludes>
                    <exclude>com/miniaml/Main.class</exclude>
                    <exclude>com/miniaml/learning/**</exclude>
                    <exclude>com/miniaml/exception/**</exclude>
                </excludes>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**跑测试**：`mvn test`

**看报告**：`target/site/jacoco/index.html`

### Git

```
git add .
git commit -m "feat: xxx"
git push
git log --oneline
git status
git restore <file>     # 撤销文件改动
```

**Commit 前缀**：

```
feat:     新功能
fix:      修 bug
docs:     文档
refactor: 重构
test:     测试
chore:    杂项
wip:      半成品
```

**提交频率**：完成一件完整的事就提交一次（每天 2~5 次）

---

## 📅 学习进度

### 2026-09-10 ~ 09-14（第 1 章）

- 环境搭建、Maven、Git、IDEA
- Customer / Account / Transaction / SuspiciousCase
- Rule 接口、LargeAmountRule、DailyAmountRule
- Main 完整流程
- GitHub 上传、README、UML 图

### 2026-09-15 ~ 09-16（第 2 章前段）

- List（add / get / size / 遍历）
- Map（put / get / containsKey / entrySet）
- Set（去重 / contains）
- fail-fast（Set 稳定抛，List 不稳定）
- 异常处理：IllegalArgumentException、try-catch、throw
- Main 重构：80 行 → 8 个方法
- Transaction 参数校验

### 2026-09-17（JUnit + Lambda）

- JUnit 5：@Test、断言、10 个测试全通过
- 测试类放 src/test/java，同包名
- Lambda：forEach / removeIf / computeIfAbsent
- groupByAccount 用 computeIfAbsent 重构

### 2026-09-18（Stream）

- Stream 三段结构：创建 → 中间 → 终止
- filter / map / collect / groupingBy
- Stream 不改原数据
- 方法引用 `Transaction::getId`

### 2026-09-20（文件 IO + CSV + DailyAmountRule 重构）

- BufferedWriter / BufferedReader
- try-with-resources 自动关闭
- Random 固定种子可复现
- 类型转换：Long.parseLong / new BigDecimal / LocalDateTime.parse
- 完成数据管道：生成 → 文件 → 读取 → 业务处理
- DailyAmountRule 重构：用 groupingBy 按日期分组
- 踩坑：变量作用域——total 在外层循环外声明会跨天累加
- 踩坑：Map 的 key 类型必须和 groupingBy 返回的 key 一致

### 2026-09-23（Smurfing 识别）

- 三层分组：账户 → 日期 → 交易列表
- 用 TreeMap 保证日期有序
- 用 ChronoUnit.DAYS.between 判断连续日期
- 滑动窗口算法
- 新建 SmurfingRule 规则：连续 3 天每天 4~5 万
- 踩坑：滑动窗口边界 off-by-one
- 踩坑：金额区间边界 `[40000, 50000)`
- 业务收获：理解阈值型 / 统计型 / 模式型三种规则

### 2026-09-24（泛型 + 覆盖率）

- 泛型规则引擎重构：Rule → Rule<T>
- 3 个实现类改成 implements Rule<List<Transaction>>
- pom.xml 加 JaCoCo 插件
- 覆盖率从 8% 提升到 88%
- 补测试：ListUtilTest / SmurfingRuleTest / CustomerTest /
  AccountTest / SuspiciousCaseTest
- 旧测试改英文命名
- 踩坑：DailyAmountRuleTest 数据过时（规则改了测试没同步）
- 踩坑：AccountTest 少写 @Test 导致测试未执行

---

## 📝 TODO

### 高优先级
- [ ] 更新 `README.md`（项目已到 v0.2）
- [ ] 清理 Main 里的学习练习方法

### 中优先级
- [ ] 补全例程库（MapExamples / ListExamples / SetExamples /
  ExceptionExamples / FileIOExamples）
- [ ] 引入日志框架（用 Log 替代 System.out.println）
- [ ] 引入 Service 层，把 Main 拆分

### 低优先级
- [ ] Maven 生命周期学习
- [ ] Maven 多模块概念

---

## 🗺️ 后续路线

```
第3章：MySQL + JDBC
  - 数据库基础、SQL
  - JDBC 连接
  - mini-aml v0.3（数据存数据库）

第4章：Spring Boot
  - Spring Boot 基础
  - REST API
  - mini-aml v0.4

第5章：MyBatis + REST
  - ORM 框架
  - 完整 REST 接口
  - mini-aml v0.5

第6章：Redis / Docker / Linux
  - 缓存、容器化、部署

第7章：Vue
  - 前端页面
  - 前后端联调

最终：mini-aml v1.0（完整系统）
      ↓
    Java 后端实习
```