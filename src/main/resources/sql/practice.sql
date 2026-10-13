SELECT name, id_card FROM customer;


SELECT id, balance FROM account WHERE balance > 100000;


SELECT * FROM `transaction` WHERE type = 'IN' ORDER BY amount DESC;


SELECT c.name, SUM(a.balance) AS total
FROM customer c
INNER JOIN account a ON a.customer_id  = c.id
GROUP BY c.name, c.id;


SELECT account_id, COUNT(*) AS cnt, SUM(amount) AS total
FROM `transaction`
GROUP BY account_id;

-- 练习 6：查询每个账户，分别显示"转入总额"和"转出总额"
SELECT
    account_id,
    SUM(CASE WHEN type = 'IN' THEN amount ELSE 0 END) AS total_in,
    SUM(CASE WHEN type = 'OUT' THEN amount ELSE 0 END) AS total_out
FROM `transaction`
GROUP BY account_id;

-- 练习 7：查询交易笔数 ≥ 3 的账户
SELECT
    account_id,
    COUNT(*) AS cnt
FROM `transaction`
GROUP BY account_id
HAVING cnt >= 3;

-- 练习 8：查询"单日交易总额 ≥ 15 万"的账户和日期
SELECT
    account_id,
    DATE(trans_time) AS day,
    SUM(amount) AS total
FROM `transaction`
GROUP BY account_id, day
HAVING total >= 150000;

-- 练习 9：查询所有"启用的"规则配置，显示规则名和阈值
SELECT
    rule_name,
    threshold
FROM rule_config
WHERE enabled = 1;

-- 练习 10：查询每个账户的最大单笔交易金额
SELECT
    account_id,
    MAX(amount) AS max_amount
FROM `transaction`
GROUP BY account_id;

-- 练习 11：给每笔交易按账户分组，账户内按时间排序，标上行号
select
	id,
	account_id,
	trans_time,
	ROW_NUMBER() OVER(partition by account_id order by trans_time) as rn
from `transaction`;

-- 练习 12：给每笔交易加上"该账户的累计金额"（按时间顺序累加）
select
	id,
	account_id,
	trans_time,
	amount,
	SUM(amount) OVER(partition by account_id order by trans_time) as cum_sum
from `transaction`;

-- 练习 13：给每笔交易加上"该账户的总金额"（不累计，每行都显示相同的账户总额）
select
	id,
	account_id,
	trans_time,
	amount,
	SUM(amount) OVER(partition by account_id) as sum
from `transaction`;

-- 练习 14：查看每笔交易的"下一笔交易金额"（按账户分组，按时间排序）
select
	id,
	account_id,
	amount,
    LEAD(amount, 1) OVER (PARTITION BY account_id ORDER BY trans_time) AS next_amount
from `transaction`;

-- 练习 15：查看每笔交易的"上一笔交易金额"

select
	id,
	account_id,
	amount,
    LAG(amount, 1) OVER (PARTITION BY account_id ORDER BY trans_time) AS prev_amount
from `transaction`;

-- 练习 16：找出每个账户中"金额最大"的那笔交易，显示 id、account_id、amount
-- 提示：用 ROW_NUMBER() 按金额倒序编号，取 rn = 1

select
	id,
	account_id,
	amount
from(
	select
		id,
		account_id,
		amount,
		ROW_NUMBER() OVER(partition by account_id order by amount desc) as rn
	from `transaction`
) as all_amount
where rn = 1;

-- 练习 17：给每个账户的每笔交易，计算"该笔金额占该账户总金额的百分比"
-- 提示：分子=amount，分母=SUM(amount) OVER (PARTITION BY account_id)
-- 输出：id、account_id、amount、percent

select
	id,
	account_id,
	amount,
	amount/(SUM(amount) OVER(partition by account_id))*100 as percent
from `transaction`;

-- 练习 18：找出每个账户中"连续两笔金额相同"的交易对
-- 提示：用 LAG(amount, 1) 看上一笔，如果和当前相同，就是一对

select
	id,
	account_id,
	amount,
	lag_amount
from(
	select
		id,
		account_id,
		amount,
		LAG(amount, 1) OVER(partition by account_id order by trans_time) as lag_amount
	from `transaction`
) as all_amount
where amount = lag_amount;


-- 练习 19：查询"交易笔数 ≥ 3 且单笔金额都 ≥ 10000"的账户
-- 提示：GROUP BY account_id，HAVING COUNT(*) >= 3 AND MIN(amount) >= 10000

select
	account_id
from `transaction`
group by account_id
having COUNT(*) >= 3 and MIN(amount) >=10000;

-- 练习 20：给每个账户的第一笔交易（按时间）标记出来
-- 提示：用 ROW_NUMBER() OVER (PARTITION BY account_id ORDER BY trans_time) = 1 筛选

select
	id,
	account_id,
	trans_time
from(
	select
		id,
		account_id,
		trans_time,
		ROW_NUMBER() OVER(partition by account_id order by trans_time) as rn
	from `transaction`
) as total_trans
where rn = 1;


