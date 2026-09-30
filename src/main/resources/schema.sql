-- ============================================
-- mini-aml 数据库表结构
-- 用途：重建数据库时执行
-- 使用方法：
--   1. mysql -u root -p
--   2. CREATE DATABASE IF NOT EXISTS mini_aml;
--   3. USE mini_aml;
--   4. source src/main/resources/schema.sql
-- ============================================

-- 按外键依赖顺序删除（先删有外键引用的表）
DROP TABLE IF EXISTS `suspicious_case`;
DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `rule_config`;

-- ============================================
-- 1. customer 客户表
-- ============================================
CREATE TABLE customer (
    id         BIGINT       PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL,
    id_card    VARCHAR(20)  NOT NULL UNIQUE,
    created_at DATETIME     NOT NULL
);

-- ============================================
-- 2. account 账户表
-- 外键：customer_id → customer.id
-- ============================================
CREATE TABLE account (
    id          BIGINT        PRIMARY KEY,
    account_no  VARCHAR(30)   NOT NULL UNIQUE,
    customer_id BIGINT        NOT NULL,
    balance     DECIMAL(15,2) NOT NULL,
    created_at  DATETIME      NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- ============================================
-- 3. transaction 交易表
-- 表名是 MySQL 关键字，需要反引号
-- 外键：account_id → account.id
-- ============================================
CREATE TABLE `transaction` (
    id          BIGINT        PRIMARY KEY,
    account_id  BIGINT        NOT NULL,
    amount      DECIMAL(15,2) NOT NULL,
    type        VARCHAR(10)   NOT NULL,
    trans_time  DATETIME      NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account(id)
);

-- ============================================
-- 4. rule_config 规则配置表
-- 存规则名、类型、阈值、是否启用
-- ============================================
CREATE TABLE rule_config (
    id          BIGINT        PRIMARY KEY,
    rule_name   VARCHAR(50)   NOT NULL UNIQUE,
    rule_type   VARCHAR(20)   NOT NULL,
    threshold   DECIMAL(15,2) NOT NULL,
    enabled     TINYINT(1)    NOT NULL DEFAULT 1,
    created_at  DATETIME      NOT NULL
);

-- ============================================
-- 5. suspicious_case 可疑案例表
-- id 自增（案例是系统自动生成的）
-- 外键：transaction_id → transaction.id
-- ============================================
CREATE TABLE suspicious_case (
    id             BIGINT       PRIMARY KEY AUTO_INCREMENT,
    case_no        VARCHAR(30)  NOT NULL UNIQUE,
    transaction_id BIGINT       NOT NULL,
    rule_name      VARCHAR(50)  NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    create_time    DATETIME     NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES `transaction`(id)
);

-- ============================================
-- 初始化数据
-- ============================================

-- 客户
INSERT INTO customer (id, name, id_card, created_at) VALUES
(1, '张三', '110101199001011234', NOW()),
(2, '李四', '110101199002022345', NOW());

-- 账户
INSERT INTO account (id, account_no, customer_id, balance, created_at) VALUES
(1, '622200001', 1, 100000.00, NOW()),
(2, '622200002', 1,  50000.00, NOW()),
(3, '622200003', 2, 200000.00, NOW());

-- 交易（覆盖三条规则的测试场景）
INSERT INTO `transaction` (id, account_id, amount, type, trans_time) VALUES
(10001, 1,  45000.00, 'IN',  '2026-09-10 10:30:00'),
(10002, 2, 180000.00, 'IN',  '2026-09-10 23:30:00'),
(10003, 2,  30000.00, 'OUT', '2026-09-11 06:30:00'),
(10004, 1,  60000.00, 'IN',  '2026-10-10 10:30:00'),
(10005, 1,  39000.00, 'IN',  '2026-09-12 10:50:30');

-- 规则配置
INSERT INTO rule_config (id, rule_name, rule_type, threshold, enabled, created_at) VALUES
(1, '单笔大额交易', 'LARGE_AMOUNT',  50000.00, 1, NOW()),
(2, '单日累计交易', 'DAILY_AMOUNT', 200000.00, 1, NOW()),
(3, '拆分交易识别', 'SMURFING',      40000.00, 1, NOW());

-- suspicious_case 表暂时为空，等程序运行时自动生成