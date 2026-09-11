1.在 src/main/java/com/miniaml/model/ 下新建 Transaction.java，
包名 com.miniaml.model，文件顶部 import 两个类 java.math.BigDecimal
和 java.time.LocalDateTime，

2.类里定义 5 个 private 字段：Long id（交易编号）、
Long accountId（属于哪个账户）、BigDecimal amount（金额）、
String type（"IN" 或 "OUT"）、LocalDateTime transTime（交易时间），

3.然后提供无参构造和全参构造（构造里用 this 区分字段和参数），

4.再写 5 个 getter（用 return，不打印）和 5 个 setter（用 this.xxx = xxx），

5.最后重写 toString() 让打印结果形如 Transaction{id=10001, accountId=1,
amount=60000.00, type='IN', transTime=2026-09-10T10:30}
（其中 type 是字符串要加单引号，其余字段不加），

6.写完后在 Main 里用
new Transaction(10001L, 1L, new BigDecimal("60000.00"), "IN",
LocalDateTime.of(2026, 9, 10, 10, 30, 0)) 创建对象并打印，
验证输出与预期一致。