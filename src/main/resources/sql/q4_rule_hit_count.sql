-- TRUNCATE TABLE suspicious_case;
--
-- INSERT INTO suspicious_case (case_no, transaction_id, rule_name, status, create_time) VALUES
-- ('CASE-0001', 10004, '单笔金额大于等于 5 万', 'NEW', DATE_SUB(NOW(), INTERVAL 1 DAY)),
-- ('CASE-0002', 10002, '单笔金额大于等于 5 万', 'NEW', DATE_SUB(NOW(), INTERVAL 2 DAY)),
-- ('CASE-0003', 10004, '单笔金额大于等于 5 万', 'NEW', DATE_SUB(NOW(), INTERVAL 3 DAY)),
-- ('CASE-0004', 10001, '拆分交易（连续3天每天4-5万）', 'NEW', DATE_SUB(NOW(), INTERVAL 5 DAY)),
-- ('CASE-0005', 10002, '单笔金额大于等于 5 万', 'NEW', DATE_SUB(NOW(), INTERVAL 10 DAY));

-- 第 4 题：统计每个规则近 7 天命中量

SELECT
    rule_name,
    COUNT(*) AS hit_count
FROM suspicious_case
WHERE create_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)
  AND create_time <= NOW()
GROUP BY rule_name;