# mini-aml 学习笔记

---

## 🎮 Java Backend RPG 快照

**更新日期**：2026-09-20

| 项目 | 内容 |
|---|---|
| 玩家 | OriginHunter |
| 等级 | Lv.10 Java 实战者 |
| XP | 2840 / 3200 |
| 金币 | 约 600 |
| 当前关卡 | 2-6 Stream API（接近完成） |

### 主线进度

```
第1章：Java 基础与 OOP           ✅ 100%
第2章：集合与进阶                 🟡 90%
  ├─ 2-1 List                     ✅
  ├─ 2-2 Map + 异常 + 重构         ✅
  ├─ 2-3 Set + fail-fast          ✅
  ├─ 2-4 JUnit 5                  ✅
  ├─ 2-5 Lambda                   ✅
  ├─ 2-6 Stream API               🟡
  ├─ 2-7 泛型深入                 ⬜
  └─ 2-8 日志                     ⬜
第3章：MySQL + JDBC              ⬜ 0%
第4章：Spring Boot               ⬜ 0%
第5章：MyBatis + REST            ⬜ 0%
第6章：Redis / Docker / Linux     ⬜ 0%
第7章：Vue                       ⬜ 0%
```

### 已解锁成就（18）

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
```

### 项目进度

```
mini-aml

v0.1 ██████████ 100%   控制台版
v0.2 █████████░ 90%    集合 + 异常 + 分组 + 测试 + CSV
v0.3 ⬜                接入 MySQL
v0.4 ⬜                Spring Boot API
v0.5 ⬜                MyBatis + REST
v1.0 ⬜                完整系统
```

---

## 📚 知识清单

### Java 基础

- `package` / `import`：类的"住址"和"引用"
- 类 / 对象 / 字段 / 方法 / 构造方法
- `private` / `public` / `protected` / 默认
- `static` / `final` / 常量
- getter / setter / `toString()`
- `this` 关键字
- `interface` 接口 / `implements` 实现
- 多态：`Rule rule = new LargeAmountRule()`

### 常用类型

| 类型 | 用途 | 注意 |
|---|---|---|
| `Long` / `Integer` | 整数（对象版） | 比较用 equals |
| `String` | 字符串 | 比较用 equals |
| `BigDecimal` | 金额 | 用字符串构造，用 compareTo 比较 |
| `LocalDateTime` | 日期时间 | ISO 格式 `2026-09-24T14:00` |
| `LocalDate` | 仅日期 | `2026-09-24` |

### 集合

- `List`（有序可重复）/ `ArrayList`
- `Set`（无序不重复）/ `HashSet`
- `Map`（键值对）/ `HashMap`
- `List<Long>` vs `long`：包装类 vs 基本类型
- 自动装箱 / 拆箱

### 异常

- `try-catch-finally`
- `throw` 抛异常
- checked（`IOException`）vs unchecked（`IllegalArgumentException`）
- `try-with-resources`（自动关闭）

### Lambda & Stream

**Lambda 心法**：`参数 -> 表达式`，读作"对于每个参数，做表达式"

| Lambda | 含义 | 用在哪 |
|---|---|---|
| `x -> System.out.println(x)` | 遍历 | `forEach` |
| `(k, v) -> ...` | 遍历 Map | `Map.forEach` |
| `x -> x < 0` | 判断 | `removeIf` / `filter` |
| `k -> new ArrayList<>()` | 生成 | `computeIfAbsent` |

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
| `forEach(Consumer)` | 遍历 |
| `sorted()` / `distinct()` / `limit(n)` | 排序 / 去重 / 取前 n |

**重要特性**：Stream 不改原集合，只输出新结果。

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

### 测试 JUnit 5

```java
@Test
void 测试名() {
    // Arrange
    // Act
    // Assert
    assertTrue(...);
    assertFalse(...);
    assertEquals(...);
    assertThrows(...);
    assertDoesNotThrow(...);
}
```

**重要点**：
- 测试类放 `src/test/java` 下，和被测类同包名
- 类名 = 被测类名 + Test
- Maven 引入依赖：`<dependency>` 加 `sync`

### Git

```
git add .
git commit -m "feat: xxx"
git push
git log --oneline
git status
```

**Commit 前缀**：

```
feat:   新功能
fix:    修 bug
docs:   文档
refactor: 重构
test:   测试
chore:  杂项
wip:    半成品
```

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
- filter（过滤）/ map（转换）/ collect（收集）
- groupingBy（分组）
- Stream 不改原数据
- 方法引用 `Transaction::getId`
- groupByAccount 用 groupingBy 重构

### 2026-09-20（文件 IO + CSV）
- BufferedWriter / FileWriter 写文件
- BufferedReader / FileReader 读文件
- try-with-resources 自动关闭
- Random 生成随机数
- Random 种子：`new Random(42)` 让数据可复现
- LocalDateTime 时间生成
- String.split 切分 CSV
- 类型转换：Long.parseLong / new BigDecimal / LocalDateTime.parse
- 完成数据管道：生成 → 文件 → 读取 → 业务处理

---

## 📝 TODO

### 高优先级
- [ ] `DailyAmountRule` 真正实现"按日期分组"
  现在：全部相加判断 ≥ 20 万
  应该：按日期分组后，每天单独判断 ≥ 20 万

### 中优先级
- [ ] 更新 `README.md`（项目已到 v0.2）
- [ ] 补充 2-7 泛型深入
- [ ] 补充 2-8 日志（用 Log 替代 System.out.println）

### 低优先级
- [ ] 代码里的 `step1` ~ `step5` 学习练习删掉
- [ ] `Main` 里 `createTransactions` 名字改为 `generateTransactions`（避免和模型类名混淆）

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