-- 第 5 题：Explain 优化慢查询

-- 加索引前
EXPLAIN SELECT * FROM `transaction` WHERE type = 'IN';
-- 结果：type=ALL, key=NULL, rows=14（全表扫描）

-- 创建索引
CREATE INDEX idx_type ON `transaction`(type);

-- 加索引后
EXPLAIN SELECT * FROM `transaction` WHERE type = 'IN';
-- 结果：type=ref, key=idx_type, rows=10（索引查找）

-- 结论：从全表扫描（ALL）优化为索引查找（ref）
--       但 type 字段选择性低（只有 IN/OUT），优化幅度有限