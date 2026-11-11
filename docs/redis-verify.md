# D1 · Redis 规则配置缓存 验收手册

> 配套代码：`RuleCacheService.java`、`RuleService.getRuleConfig/updateRuleConfig`、`RuleController` 的 `GET/PUT /api/rules/{id}`。
> 编译已通过（Maven 3.9.16 + JDK 17，`BUILD SUCCESS`）。

---

## 0. 前置：VM 上装 Redis

SSH 进 `192.168.100.128`（CentOS 7）：

```bash
sudo yum install -y redis
sudo systemctl enable --now redis
redis-cli ping          # 期望 PONG
```

让 Redis 允许远程连（默认只听 127.0.0.1）：

```bash
sudo vi /etc/redis.conf
#   bind 127.0.0.1  ->  bind 0.0.0.0
#   protected-mode yes -> protected-mode no
sudo systemctl restart redis
```

放行 6379（CentOS 7 的 firewalld）：

```bash
sudo firewall-cmd --permanent --add-port=6379/tcp
sudo firewall-cmd --reload
```

回到本机验证端口通了再往下走：

```powershell
Test-NetConnection 192.168.100.128 -Port 6379
```

> 只改 `bind` 不改 `protected-mode`，远程连会报
> `DENIED Redis is running in protected mode`——这是 D1 最常见的坑。

---

## 1. 验收点一：缓存命中（第二次查询不打 SQL）

启动应用，打开 `RuleService` 里的日志，连续查两次同一条规则：

```powershell
curl.exe -s http://localhost:8081/api/rules/1
curl.exe -s http://localhost:8081/api/rules/1
```

**期望**：第一次日志出现 `缓存未命中，回源查库，ruleId=1`，第二次没有；MySQL 侧只有一条 `SELECT`。

查一下 key 真的进去了：

```bash
redis-cli --scan --pattern 'aml:rule:*'
redis-cli ttl aml:rule:1        # 期望 1800 上下
redis-cli get aml:rule:1        # 期望是一段 JSON
```

---

## 2. 验收点二：改阈值立即生效（先更库、再删缓存）

```powershell
curl.exe -s -X PUT http://localhost:8081/api/rules/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"threshold\": 30000}"
```

**期望**：
1. `redis-cli get aml:rule:1` 立刻返回 `(nil)`（被 evict 掉了）；
2. 再 `GET /api/rules/1` 拿到的是 30000，日志里出现一次"缓存未命中"；
3. `SELECT threshold FROM rule_config WHERE id=1;` 也是 30000——库和缓存一致。

这就是"先更新数据库、再删除缓存"的可见效果。如果顺序反过来，删缓存到回源之间有个窗口，并发请求会把旧值重新写回去，缓存就长期是脏的。

---

## 3. 验收点三：Redis 挂了不影响业务（降级）

在 VM 上停掉 Redis：

```bash
sudo systemctl stop redis
```

再查规则：

```powershell
curl.exe -s http://localhost:8081/api/rules/1
```

**期望**：仍然返回 200 和正确的规则数据——`RuleCacheService.get()` 捕获连接异常后返回 `Optional.empty()`，`RuleService` 回源查库。日志里有一条 WARN，但接口不 500。

这条是面试/答辩的高频追问："缓存不可用会不会拖垮数据库？" 你的答案是"降级回源，同时至少能保证功能不中断"，并主动补一句"生产上还要加熔断和本地缓存兜底"。

验收完记得 `sudo systemctl start redis`。

---

## 4. 顺手做掉的两件事

- 跑一次跑批再查案例列表，确认 `hitCount` 累加后缓存已失效（`redis-cli get aml:rule:1` 为 nil），页面上的命中数是最新的。
- 在 IDEA 里跑一遍 `mvn test`，确认原有 4 个 mapper 测试没被这次改动打破。

---

## 5. 提交

```powershell
git checkout -b feature/redis-cache      # 还没建分支的话
git add mini-aml-springboot/
git commit -m "feat: cache rule config in redis with cache-aside and evict-on-write"
git push -u origin feature/redis-cache
```

> 注意：`application.properties` 已被 `.gitignore` 排除（里面有 VM IP 和密码），
> 所以这次提交里看不到 Redis 配置——正常，配置属于环境，不进仓库。
