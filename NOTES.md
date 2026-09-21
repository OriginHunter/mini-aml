### 2026-09-21（任务 1：自定义异常 + 脏数据检测）

- 新建 InvalidTransactionException（继承 RuntimeException）
- 理解 unchecked 异常的价值（代码简洁、上层统一处理）
- loadTransactions 加脏数据检测：
    - 字段个数检查
    - 类型转换错误捕获（NumberFormatException / DateTimeParseException）
    - 交易类型检查
    - 记录行号，抛出自定义异常
- 验证：故意造脏数据，程序正确报错

### 踩坑
- `string[0].length()` 是"字段的字符长度"，不是"字段个数"
  → 应该是 `string.length`（数组长度）
### 自定义异常

- 继承 RuntimeException（unchecked）或 Exception（checked）
- 提供两个构造方法：`(String message)` 和 `(String message, Throwable cause)`
- 抛：`throw new XxxException("消息")`
- 接：`catch (XxxException e) { ... }`
- 现代 Java 项目倾向 unchecked（代码简洁，Spring 全局处理）