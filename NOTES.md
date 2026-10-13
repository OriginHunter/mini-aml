表	索引	用途
customer	PRIMARY	主键唯一标识
customer	id_card (UNIQUE)	身份证不能重复
account	PRIMARY	主键唯一标识
account	account_no (UNIQUE)	账号不能重复
account	customer_id（外键）	JOIN customer 时加速
transaction	PRIMARY	主键唯一标识
transaction	account_id（外键）	JOIN account 时加速
transaction	idx_type	按类型筛选（IN/OUT）
transaction	idx_type_amount	类型 + 金额的组合查询
transaction	idx_trans_time	按时间范围查交易
transaction	idx_account_time	某账户某时间段的交易（规则判断）
rule_config	PRIMARY	主键唯一标识
rule_config	rule_name (UNIQUE)	规则名不能重复
suspicious_case	PRIMARY	主键唯一标识
suspicious_case	case_no (UNIQUE)	案例编号不能重复
suspicious_case	transaction_id（外键）	JOIN transaction 时加速
suspicious_case	idx_status	查"待处理案例"
suspicious_case	idx_create_time	查"近 N 天的案例"