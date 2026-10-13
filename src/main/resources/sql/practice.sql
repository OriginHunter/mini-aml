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


-- ============================================
-- 第一批：建表 SQL（DDL）练习
-- ============================================

-- 练习 1：建 student 表
-- 字段：id（BIGINT 主键）、name（VARCHAR(50) 不能为空）、age（INT）
CREATE TABLE student (
    id    BIGINT  PRIMARY  KEY,
    name  VARCHAR(50)  NOT  NULL,
    age   INT
);


-- 练习 2：建 class 表
-- 字段：id（BIGINT 主键）、class_name（VARCHAR(50) 不能为空且唯一）
CREATE TABLE `class` (
    id          BIGINT  PRIMARY  KEY,
    class_name  VARCHAR(50)  NOT  NULL  UNIQUE
);


-- 练习 3：建 student_class 表
-- 字段：id（BIGINT 主键）、student_id（BIGINT 不为空）、class_id（BIGINT 不为空）
-- 外键：student_id → student.id，class_id → class.id
CREATE TABLE student_class (
    id          BIGINT  PRIMARY  KEY,
    student_id  BIGINT  NOT  NULL,
    class_id    BIGINT  NOT  NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (class_id) REFERENCES `class`(id)
);


-- 练习 4：给 student 表加 gender 字段
-- 字段：gender（VARCHAR(10) 不为空，默认 '未知'）
ALTER TABLE student
ADD COLUMN gender  VARCHAR(10)  NOT  NULL DEFAULT '未知';


-- 练习 5：删除 student_class 表
DROP TABLE student_class;

-- ============================================
-- 第二批：DML 练习
-- 用表：student / class / student_class
-- ============================================


-- 练习 6：往 student 表插入一条数据
-- 数据：id=1, name='张三', age=18, gender='男'
INSERT INTO student (id, name, age, gender)
VALUES (1, '张三', 18, '男');


-- 练习 7：往 student 表一次插入 3 条数据
-- 数据：
--   (2, '李四', 19, '男')
--   (3, '王五', 18, '女')
--   (4, '赵六', 20, '男')
INSERT INTO student (id, name, age, gender) VALUES
(2, '李四', 19, '男'),
(3, '王五', 18, '女'),
(4, '赵六', 20, '男');


-- 练习 8：把 student 表里 id=1 的学生年龄改成 21
UPDATE student
SET age = 21
WHERE id = 1;


-- 练习 9：把所有 gender='男' 的学生的年龄 +1
UPDATE student
SET age = age + 1
WHERE gender = '男';


-- 练习 10：删除 student 表里 id=4 的学生
DELETE FROM student
WHERE id = 4;


-- 练习 11：查询 student 表所有数据
SELECT * FROM student;


-- 练习 12：查询 student 表里年龄大于等于 19 的学生，显示 id、name、age
SELECT id, name, age
FROM student
WHERE age >= 19;


-- 练习 13：查询 student 表里的学生，按年龄从大到小排序，年龄相同时按 id 升序
SELECT *
FROM student
ORDER BY age DESC, id;

-- 给 class 表插 3 条数据
INSERT INTO class (id, class_name) VALUES
(1, '一班'),
(2, '二班'),
(3, '三班');

-- 给 student_class 表插选课关系
INSERT INTO student_class (id, student_id, class_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 3, 2),
(5, 3, 3),
(6, 4, 1),
(7, 4, 2),
(8, 4, 3);

SELECT * FROM student;
SELECT * FROM class;
SELECT * FROM student_class;

-- ============================================
-- 第三批：JOIN + GROUP BY + 聚合
-- 用表：student / class / student_class
-- ============================================


-- 练习 14：两表 JOIN
-- 需求：查所有选课记录，显示 student_class.id、student.name、class.class_name
-- 提示：student_class JOIN student JOIN class
select
	sc.id,
	s.name,
	c.class_name
from student_class sc
inner join student s on sc.student_id = s.id
inner join class c on sc.class_id = c.id;

-- 练习 15：三表 JOIN
-- 需求：查"张三"选了哪些课，显示课程名
-- 提示：WHERE student.name = '张三'

select
	s.name,
	c.id,
	c.class_name
from student_class sc
inner join student s on sc.student_id = s.id
inner join class c on sc.class_id = c.id
where s.name = '张三';

-- 练习 16：LEFT JOIN
-- 需求：查所有学生，显示学生名 + 他选的课名
-- 没选课的学生也要显示（课名显示 NULL）
-- 提示：以 student 为主表 LEFT JOIN

select
	s.name,
	c.class_name
from student s
left join student_class sc on s.id = sc.student_id
left join class c on sc.class_id = c.id;

-- 练习 17：GROUP BY + COUNT
-- 需求：每个班级有多少学生
-- 输出：class_name、student_count
-- 提示：GROUP BY class.id, class.class_name

select
	c.id,
	c.class_name,
	COUNT(s.id) as student_count
from student_class sc
inner join student s on sc.student_id = s.id
inner join class c on sc.class_id = c.id
group by c.id, c.class_name;

-- 练习 18：GROUP BY + COUNT
-- 需求：每个学生选了几门课
-- 输出：student.name、class_count
-- 提示：GROUP BY student.id, student.name

select
	s.name,
	s.id,
	COUNT(c.class_name) as class_count
from student_class sc
inner join student s on sc.student_id = s.id
inner join class c on sc.class_id = c.id
group by s.name,s.id;


-- 练习 19：GROUP BY + 聚合（SUM / AVG / MAX / MIN）
-- 需求：每个班级的"学生平均年龄"
-- 输出：class_name、avg_age
-- 提示：JOIN 三张表 + GROUP BY + AVG

select
	c.id,
	c.class_name,
	AVG(s.age)
from student_class sc
inner join student s on sc.student_id = s.id
inner join class c on sc.class_id = c.id
group by c.id, c.class_name ;

-- 练习 20：HAVING
-- 需求：查询"选课数 ≥ 2"的学生
-- 输出：student.name、class_count
-- 提示：GROUP BY + HAVING COUNT(*) >= 2

select
	s.id,
	s.name,
	COUNT(c.id) as class_count
from student_class sc
inner join student s on sc.student_id = s.id
inner join class c on sc.class_id = c.id
group by s.id, s.name
having class_count >= 2;



