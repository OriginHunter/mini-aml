-- 第 3 题：窗口函数找拆分交易序列（连续 3 天每天 4~5 万）

SELECT 
    account_id, day, daily_total,
    next_day_1, next_day_2
FROM (
    SELECT 
        account_id, day, daily_total,
        LEAD(day, 1) OVER (PARTITION BY account_id ORDER BY day) AS next_day_1,
        LEAD(day, 2) OVER (PARTITION BY account_id ORDER BY day) AS next_day_2,
        LEAD(daily_total, 1) OVER (PARTITION BY account_id ORDER BY day) AS next_total_1,
        LEAD(daily_total, 2) OVER (PARTITION BY account_id ORDER BY day) AS next_total_2
    FROM (
        SELECT 
            t.account_id,
            DATE(t.trans_time) AS day,
            SUM(t.amount) AS daily_total
        FROM `transaction` t
        GROUP BY t.account_id, DATE(t.trans_time)
    ) AS daily
) AS with_lead
WHERE next_day_1 IS NOT NULL
  AND next_day_2 IS NOT NULL
  AND DATEDIFF(next_day_1, day) = 1
  AND DATEDIFF(next_day_2, next_day_1) = 1
  AND daily_total BETWEEN 40000 AND 49999.99
  AND next_total_1 BETWEEN 40000 AND 49999.99
  AND next_total_2 BETWEEN 40000 AND 49999.99;