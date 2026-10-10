-- 第 1 题：查某客户近 30 天交易总额

SELECT
    c.name,
    SUM(t.amount) AS total_amount
FROM customer c
INNER JOIN account a ON a.customer_id = c.id
INNER JOIN `transaction` t ON t.account_id = a.id
WHERE c.name = '张三'
  AND t.trans_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
  AND t.trans_time <= NOW()
GROUP BY c.id, c.name;