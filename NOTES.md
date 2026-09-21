🎉 任务 1：CSV 脏数据处理 + 自定义异常

任务清单：
✅ 自定义 InvalidTransactionException +20 XP
✅ 理解 RuntimeException vs Exception +15 XP
✅ loadTransactions 加脏数据检测 +30 XP
✅ 修复"字段个数"检查 bug +15 XP
✅ 验证脏数据抛异常 +20 XP
✅ 行号定位正确 +10 XP
✅ main 里加 catch +25 XP（新增）
✅ 理解 throw 和 catch 的角色分工 +20 XP

小计：+155 XP
关卡完成奖励：+100 XP
总计：+255 XP

当前 XP：3000 + 255 = 3255 / 4000
Lv.11  Java 后端实习生候选人
🏆 异常架构师       — 掌握自定义异常的设计与使用
🏆 数据守门员       — 掌握脏数据检测与报告
🏆 异常处理流程师    — 掌握 throw → catch 完整闭环
### 2026-09-21（任务 1：自定义异常 + 脏数据检测）

- 新建 InvalidTransactionException（继承 RuntimeException）
- 理解 unchecked 异常的价值
- loadTransactions 加脏数据检测：
    - 字段个数检查（string.length != 5）
    - 类型转换错误捕获
    - 交易类型检查
    - 记录行号，抛自定义异常
- main 里 catch (InvalidTransactionException) 优雅处理

### 关键理解
- throw = 上报问题
- catch = 处理问题
- 程序崩的原因：有人 throw，但没人 catch
- JDK 内部帮你 throw（比如 new FileReader 抛 IOException）
- 业务错误要自己 throw

### 踩坑
- `string[0].length()` 是"字段的字符长度"，不是"字段个数"
  → 应该是 `string.length`