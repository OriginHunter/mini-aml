-- 第 2 题：找日内资金快进快出账户（进出相抵 ≥ 80%）

SELECT
    t.account_id,
    DATE(t.trans_time) AS day,
    SUM(CASE WHEN t.type = 'IN'  THEN t.amount ELSE 0 END) AS sum_in,
    SUM(CASE WHEN t.type = 'OUT' THEN t.amount ELSE 0 END) AS sum_out
FROM `transaction` t
GROUP BY t.account_id, DATE(t.trans_time)
HAVING sum_in > 0 AND sum_out > 0
   AND ABS(sum_in - sum_out) / (sum_in + sum_out) <= 0.2;