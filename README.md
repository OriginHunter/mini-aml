## 当前版本 v0.3

相比 v0.2，新增：

- **数据持久化**：交易数据存储于 MySQL 数据库
  - 数据库：mini_aml
  - 表：transactions
- **JDBC 数据库操作**
  - SELECT / INSERT / UPDATE / DELETE 完整实现
  - 使用 PreparedStatement 占位符防 SQL 注入
- **配置分离**
  - 数据库配置抽到 db.properties（不进 Git）
  - 提供 db.properties.example 模板
  - DbConfig 工具类读取配置

## 技术栈

- Java 17
- Maven
- MySQL 8.4
- JDBC（mysql-connector-j）
- JUnit 5 + JaCoCo
- Git