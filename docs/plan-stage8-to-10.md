# mini-aml 后续计划：第 8 阶段收尾 → 第 9 阶段业务 → 第 10 阶段实战

> 制定时间：2026-11-10　适用周期：约 3 周（15 个工作日，按每天 5~6 小时有效学习计）
> 对照文档：《反洗钱业务开发实习生培养方案》第 8/9/10 阶段

---

## 0. 先做一件事（今晚 30 分钟）

当前 `D:\Project\mini-aml` 有未提交改动，明天加 Redis 会把它和新代码搅在一起，出问题无法二分定位：

```
modified:   mini-aml-springboot/src/main/java/com/miniaml/controller/RuleController.java
modified:   mini-aml-springboot/src/test/java/com/miniaml/mapper/SuspiciousCaseMapperTest.java
new file:   mini-aml-springboot/src/main/java/com/miniaml/config/WebConfig.java
```

`WebConfig` 是一次性的调试产物（`allowedOriginPatterns("*")` + `allowCredentials(true)`），两个分支都必须提交，否则跨域问题会在 Docker 阶段以更隐蔽的方式复现。

```bash
git add -A && git commit -m "chore: add CORS config and fix mapper test assertion"
```

---

## 1. 现状核对：哪些是真通关，哪些只是"跑通过一次"

结论先说：**第 5、6 阶段是真通关**（代码、单测、事务回滚都有痕迹）；**第 7 阶段只完成了一半**；**第 8 阶段的 Linux/Nginx 部分在仓库里没有任何证据**。按培养方案的验收标准，现在的状态是"v1.1 + 一个能跑的静态看板"，不是 v1.2。

| 方案要求 | 仓库里的实际情况 | 判定 |
| --- | --- | --- |
| Spring Boot REST + 统一返回体 + 全局异常 | `common/Result.java`、`exception/GlobalExceptionHandler.java`、`@Validated` 齐全 | ✅ 真通关 |
| MyBatis-Plus + XML 联查 + 事务 | `SuspiciousCaseMapper.xml` 四表联查、分页、`@Transactional(rollbackFor)`、回滚单测 | ✅ 真通关 |
| Vue 3 看板 + Axios 调用 | `D:\Project\mini-aml-frontend\src\App.vue` 已是 Element Plus 表格 + 服务端分页 + axios 调 `/api/cases/page`，`dist` 已产出（JS 1.0MB / CSS 356KB） | ✅ 渲染层真做了 |
| 路由 / Pinia / 登录 / 详情 / 规则页 / Axios 封装 | `package.json` 只有 vue/axios/element-plus；`vite.config.js` 无 proxy；请求地址硬编码 `http://localhost:8081` | ⚠️ **缺一半** |
| Redis 三项落地 | 无任何 Redis 依赖/配置/代码 | ❌ 未开始（符合你的自我判断） |
| Docker 一键拉起 | 仓库内无 `Dockerfile`、`docker-compose.yml` | ❌ 未开始 |
| Nginx 反向代理 + 静态托管 | 仓库内无 `nginx.conf`（配置在虚拟机上，没进版本库） | ⚠️ 做过但没沉淀 |
| 部署文档 | `README.md` 仍停留在 v0.4，写的是 JDBC 控制台版 | ❌ 缺失 |

三个必须先修的"演示级"问题（都会在第 9 阶段答辩现场被问到）：

1. **`RuleService.java:41-44` 的规则执行是假的**：不管传哪个规则 id，一律走 `amount > threshold`；`SmurfingRule`、`DailyAmountRule` 实现了 `Rule<T>` 接口却从未被调用（只有 `LargeAmountRule` 被 `TransactionService` 注入了）。答辩时被问"你的规则引擎在哪"，现在答不出来。
2. **`RuleService.java:53-56` 埋着"模拟宕机"代码**：`if (transactionList.size() > 100) throw new RuntimeException("模拟跑批中途宕机！")`。这是给事务测试用的坑，**演示前必须删掉或挪到 `@Profile("test")`**，否则数据量一上来跑批必炸。
3. **`application.properties:4-6` 硬编码 `192.168.100.128` + 明文密码 `1234`**。Docker 阶段必须改成 `${MYSQL_HOST:...}` 形式，否则 compose 起不来，也过不了"数据敏感性"这一关。

> 这不是打击你——恰恰相反：**把这三处修掉，你的项目才从"能演示"变成"能被评审"**。第 9 阶段的答辩考的就是这个。

---

## 2. 三周后的目标（可验收）

1. 一台干净机器 + 一份 `deploy.md`，**30 分钟内** `docker compose up -d` 拉起 MySQL + Redis + 后端 + Nginx，浏览器点通"登录 → 筛选案例 → 查看详情 → 触发跑批"。
2. 跑批接口具备幂等/防重：同一规则并发点 10 次，只产生 1 批案例，其余返回"任务执行中"。
3. 规则配置读 Redis 缓存，改配置后缓存失效，命中阈值实时生效（可用日志/接口验证）。
4. 有一个**名单筛查**功能（哪怕简化），把 KYC → 监测 → 案例 → 上报串成一条能讲的链路。
5. 30 分钟架构答辩：一张架构图 + 一条业务链路 + 五个坑的复盘。

---

## 3. 15 天日程表

| 天 | 主题 | 主要产出 | 当天验收（自测） |
| --- | --- | --- | --- |
| D1 | Redis 接入 + 规则缓存 | `RedisConfig`、`RuleCacheService`、缓存读写 | 改阈值 → 立即生效；`redis-cli keys` 能看到 key |
| D2 | SETNX 分布式锁 | `@interface BatchLock` + AOP + `BatchLockAspect` | `ab -n 20 -c 10` 并发打跑批，只有 1 次真执行 |
| D3 | 限流 + 锁的正确性 | 计数器限流（Lua 或 `increment`+`expire`）、锁续期/释放 Lua | 第 11 次请求返回 429；kill -9 后锁 30s 自动释放 |
| D4 | Dockerfile + 后端镜像 | 多阶段 `Dockerfile`、`.dockerignore` | `docker build` 出 ~200MB 镜像，容器内能连库 |
| D5 | compose 编排 | `docker-compose.yml`（mysql/redis/backend/nginx）+ `init.sql` + `.env` | `docker compose up -d` 全绿，`docker compose ps` 四个 healthy |
| D6 | 前端产物进 Nginx + 排障 | `dist` 拷贝、`nginx.conf` 进仓库、healthcheck 依赖顺序 | 重启整机后一条命令恢复 |
| D7 | 部署文档 + 故障演练 | `deploy.md`、`troubleshooting.md`；自己 kill 一个容器定位恢复 | 照文档从零部署一遍，计时 ≤30 分钟 |
| D8 | Vue 补齐（1）：路由 + 拦截器 | `vue-router`、`pinia`、`src/api/request.js`、登录页、`vite.config.js` proxy | 登录拿 mock token 跳列表；拦截器能拦 401 |
| D9 | Vue 补齐（2）：列表 + 详情 | App.vue 拆 `views/CaseList.vue`、`views/CaseDetail.vue`，补三条件筛选、后端 `GET /api/cases/{id}` | "登录 → 筛选 → 详情"闭环跑通 |
| D10 | Vue 补齐（3）+ 打包优化 | 规则配置页、跑批按钮（loading + 409 提示）、Element Plus 按需引入、前端入 git | 改阈值后跑批，案例数变化符合预期；dist JS ≤400KB |
| D11 | AML 业务体系化 | `docs/aml-domain.md`：KYC→监测→STR/CTR→筛查→案例上报；对照表 | 能口述闭环，指出 mini-aml 缺哪 5 块 |
| D12 | 名单筛查模块 | `sanctions_list` 表 + `/api/screening/check` + 相似度匹配 | 输入"张伟"命中 2 条制裁名单，返回分数 |
| D13 | 案例研判 + STR 报告 | 案例状态机 `NEW→REVIEWING→ESCALATED→CLOSED` + STR 导出 | 前端能改状态，导出的 STR 含必要字段 |
| D14 | 架构图 + 答辩稿 | 拓扑图（Nginx/后端/MySQL/Redis/前端）、链路时序图、5 页 PPT | 30 分钟自讲一遍，录音回放 |
| D15 | 补课 + 缓冲 | 答辩问题清单逐条答；补齐 `README` 到 v2.0 | 同学/自己挑 10 个问题，答不上来的列清单 |

> 如果时间充裕，D10 之后插一天"代码整洁"：`mvn test` 全绿、去掉所有 `System.out`、`README` 重写。这一天的投入产出比最高。

---

## 4. 分块详解

### A. Redis 三件套（D1–D3）

**依赖**：`pom.xml` 加 `spring-boot-starter-data-redis`。你的项目是 Spring Boot 4.1.1（`pom.xml:8`），Boot 4 把部分 starter 拆成了模块（你已经踩过一次：web 用的是 `spring-boot-starter-webmvc` 而不是 `spring-boot-starter-web`）。所以**先跑 `mvn dependency:resolve` 确认能解析**，拉不到就退回 `spring-boot-data-redis` 模块 + Lettuce。这一步本身就是一个值得写进日报的坑。

**配置**（`application.properties`）：

```properties
spring.data.redis.host=${REDIS_HOST:127.0.0.1}
spring.data.redis.port=${REDIS_PORT:6379}
spring.data.redis.password=${REDIS_PASSWORD:}
spring.data.redis.timeout=2s
```

**① 规则配置缓存**：`RuleService.executeRule` 里 `ruleConfigMapper.selectById(id)` 改成先查 `aml:rule:{id}`，未命中回源并按 `ruleConfig:ttl=10m` 写入；规则更新接口里 `redis.delete(key)`。这一步的验收点是"改完阈值立刻生效"，而不是"代码里出现了 Redis"。

**② 跑批防重（这是重点，也是面试必问）**：

```java
String key = "aml:batch:lock:" + ruleId;
Boolean ok = stringRedisTemplate.opsForValue()
        .setIfAbsent(key, uuid, Duration.ofMinutes(5));   // SETNX + EXPIRE 必须原子
if (Boolean.FALSE.equals(ok)) {
    throw new BizException(409, "该规则正在跑批中，请稍后再试");
}
try { ... } finally {
    // 必须 Lua 比对 value 再删，否则会删掉别人的锁
}
```

四个必须能讲清的点：① `setIfAbsent(key, value, ttl)` 才有原子性，`setnx` + 单独 `expire` 中间宕机就死锁；② 释放锁要 `GET 比对 + DEL`，用 Lua 保证原子；③ 锁的粒度是 `ruleId` 而不是全局，否则两个规则互相阻塞；④ 业务耗时接近 TTL 时要么续期（看门狗），要么把 TTL 设成最大执行时间的 2 倍。**你被问到"锁过期了业务还没跑完怎么办"，要能答出这三档方案。**

**③ 限流**：`increment` + 首次 `expire`，或直接上 Lua。接口选跑批接口，10 次/分钟，第 11 次返回 429 + `Retry-After`。

### B. Docker 容器化（D4–D6）

- **后端 Dockerfile 用多阶段构建**：`maven:3.9-eclipse-temurin-17` 编译 → `eclipse-temurin:17-jre` 运行，只拷 `target/*.jar`。别把 `target` 和 `~/.m2` 打进镜像。
- **compose 四个服务**：`mysql:8.4`（挂 `init.sql`、`./data/mysql`、healthcheck `mysqladmin ping`）、`redis:7-alpine`（`--requirepass`、`appendonly yes`）、`backend`（`depends_on: condition: service_healthy`）、`nginx`（挂 `dist` 和 `nginx.conf`）。用 `.env` 管密码，`application.properties` 全部 `${ENV:default}`。
- **昨晚踩的 Nginx 坑要复现一遍并在 compose 里验掉**：`location /api/ { proxy_pass http://backend:8080; }` 末尾带不带 `/` 决定路径前缀是否被吃掉——这就是你上次 404 的根因。写进 `troubleshooting.md`。
- **SPA 回退**：`try_files $uri $uri/ /index.html;`，否则刷新 `/cases/1` 直接 404。
- **故障演练**（方案里明确要求的验收项）：`docker kill mini-aml-backend`，只用 `docker compose logs` + `docker ps` 定位，3 分钟内恢复。**演练时把命令和输出贴进 `troubleshooting.md`**，答辩时这就是最好的素材。

### C. 部署文档（D7）

`deploy.md` 必须包含：前置条件（Docker 版本）、`git clone` → `cp .env.example .env` → 改密码 → `docker compose up -d` → 验证四步（`ps` / `logs` / `curl /api/...` / 浏览器）→ 常见故障表。**验收方式只有一个：找一台干净机器，掐表，30 分钟内跑通。** 你已经有 CentOS 7 虚拟机，把 compose 装上去重跑一遍即可；CentOS 7 的 Docker 源和 `seccomp` 在旧内核上会有坑，正好练手。

### D. Vue 3 补齐（D8–D10）

现状：`D:\Project\mini-aml-frontend\src\App.vue` 已经是一个能跑的案例列表页——Element Plus `el-table` + `el-pagination`、服务端分页、`axios.get('http://localhost:8081/api/cases/page')`、`onMounted` 首次加载、`@current-change` 翻页，代码注释也写得很清楚。**所以这不是"重写"，是"补齐工程化"**，三天足够，甚至可以压到两天。

缺的四样，正好对应方案第 7 阶段验收标准里"完整走通登录 → 筛选案例 → 查看详情"和"能讲清拦截器里发生了什么"：

- **D8｜基础设施**：`pnpm add vue-router pinia`；`src/api/request.js` 建 axios 实例（`baseURL: '/api'`，`timeout: 10s`），请求拦截器塞 token，响应拦截器统一判 `code !== 200` 弹 `ElMessage`、401 清 token 跳登录；`router/index.js` 加 `beforeEach` 守卫；登录页写死 `admin/123456` 换 mock token 存 Pinia（顺带想清楚"token 存 localStorage 的 XSS 风险，为什么不存内存 + refresh token"）。
  - 顺手把 `vite.config.js` 加 `server.proxy: { '/api': 'http://localhost:8081' }`，这样开发环境也走相对路径，和 Nginx 生产环境的行为一致——**这一步现在不做，Docker 阶段必然返工**。
- **D9｜列表 + 详情**：App.vue 拆成 `views/CaseList.vue` + `views/CaseDetail.vue`（详情先用 `route.params.id` 从列表行带过来的数据渲染，后端补一个 `GET /api/cases/{id}` 更好）；列表页补三个筛选条件（日期范围 / 风险等级 / 状态），对应后端已有的多条件分页。
- **D10｜规则配置页 + 跑批按钮**：`GET/PUT /api/rules` 改阈值，`POST /api/rules/{id}/execute` 触发跑批，把后端 409（锁占用）渲染成"任务执行中"的提示——**前后端在这里第一次因为 Redis 产生交互，答辩时这是个好故事**。同一天做打包优化：把全量 `app.use(ElementPlus)` 换成 `unplugin-vue-components` 按需引入，`dist` 的 JS 会从 1.0MB 掉到 300KB 左右，`nginx.conf` 里再加 `gzip on`。

另外两件事：`frontend/*.html` 静态页别删，README 里注明"v1.2 起被 Vue 取代，保留作为学习痕迹"；`mini-aml-frontend` **目前不在 git 里**（没有 `.git`，也不是 mini-aml 的子目录），D10 结束前要么 `git init` 单独建仓，要么并进主仓库的 `web/` 目录——方案第 9 阶段的"分支模型/PR 规范"就靠它练。

### E. AML 业务闭环（D11–D13）

方案里写得很清楚：**技术栈通了之后，重心必须从"怎么写代码"转到"业务"**。D11 先写 `docs/aml-domain.md`，把这条链路画出来并逐段对照自己的代码：

```
客户准入(KYC/客户风险评级) → 交易监测(规则引擎/模型) → 可疑案例(命中/研判)
   → 可疑报告(STR 大额/可疑 · CTR 大额现金) → 名单筛查(制裁/PEP/负面新闻) → 上报监管
```

对照表要能答出"mini-aml 简化掉了什么"：名单筛查、客户风险评级、跑批调度（真实系统是调度平台 + 分片）、案例工单流转、结果回写核心系统、报送报文格式（如人行反洗钱监测分析二代系统的报文规范）。**这六个词答得出来，你就比 90% 的实习生懂业务。**

D12 的名单筛查做一个最小闭环就够：`sanctions_list(id, name, name_en, id_no, list_type[SANCTION/PEP/ADVERSE], country, source, create_time)`，`POST /api/screening/check` 入参姓名+证件号，出参命中列表 + 相似度分数。匹配算法先用"精确 + 去空格/全半角归一化 + 编辑距离 ≤1"，讲得清"为什么不直接 `like '%张%'`"（误报率、性能）即可。

D13 的案例状态机是真实业务的骨架：`NEW → REVIEWING → ESCALATED（报 STR）→ CLOSED（排除）`，每次流转记 `case_operate_log`（谁、何时、从什么状态到什么状态、理由）。STR 导出哪怕只导一个 txt/PDF，也要包含：报告主体、可疑特征描述、涉及交易明细、研判结论。

### F. 架构答辩（D14–D15）

30 分钟按这个结构准备（每页 5 分钟）：

1. **一张拓扑图**：浏览器 → Nginx（静态 + `/api` 反代）→ Spring Boot（Controller/Service/Mapper）→ MySQL + Redis；标出锁、缓存、限流三个点各在哪。
2. **一条业务链路**：点"跑批"按钮 → 拿锁 → 查规则（缓存命中）→ 查交易 → 命中生成案例 → 事务提交 → 计数 +1 → 释放锁 → 前端刷新。**讲的时候要能说出每一步失败会怎样。**
3. **三个设计取舍**：为什么用 SETNX 而不是数据库唯一索引防重；为什么缓存规则配置而不是交易数据；为什么案例表要逻辑删除。
4. **五个坑的复盘**（这是加分项，不是减分项）：Nginx `proxy_pass` 斜杠、500 白页、进程假死、权限、compose 启动顺序依赖。每个坑讲"现象 → 定位手段 → 根因 → 怎么防"。
5. **知道哪里不够**：单机锁 vs Redisson 红锁、没做消息队列削峰、没做分库分表、规则引擎是硬编码不是 DSL。**主动说缺点比被问出来强十倍。**

---

## 5. 演示前技术债清理清单（按优先级）

| 优先级 | 事项 | 位置 |
| --- | --- | --- |
| P0 | 删除/隔离"模拟跑批宕机"代码 | `mini-aml-springboot/src/main/java/com/miniaml/service/RuleService.java:53-56` |
| P0 | 数据库连接改环境变量，去明文密码 | `mini-aml-springboot/src/main/resources/application.properties:4-6` |
| P1 | 规则执行接上 `Rule<T>` 接口，按 `rule_code` 分发，别再用 `amount > threshold` 通吃 | `RuleService.java:41-44` |
| P1 | `CORS` 的 `*` + `allowCredentials(true)` 在 Nginx 同源部署后应删除 | `config/WebConfig.java:12-15` |
| P1 | 前端请求地址硬编码 `http://localhost:8081`，部署后必炸；改 `baseURL: '/api'` + Vite proxy | `D:\Project\mini-aml-frontend\src\App.vue:75` |
| P1 | 前端项目不在 git 里（无 `.git`，也不在主仓目录下），分支/PR 规范没法练 | `D:\Project\mini-aml-frontend` |
| P2 | Element Plus 全量引入导致 dist JS 1.0MB，改按需引入 | `D:\Project\mini-aml-frontend\src\main.js:4-8` |
| P2 | README 从 v0.4 更新到 v2.0（现状写的是 JDBC 控制台版） | `README.md` |
| P2 | 补 `docs/` 目录：`aml-domain.md`、`deploy.md`、`troubleshooting.md` | 新增 |

---

## 6. 机制建议（比技术更影响成长速度）

- **日报三行**：今天做了什么 / 卡在哪 / 明天计划。卡点写具体命令和报错原文，别写"Redis 有点难"。
- **每个阶段结束写一篇复盘**（400 字即可）：现象、定位过程、根因、下次怎么避免。你现在已经有 5 个坑，够写一篇很好的《Nginx 部署踩坑记》。
- **AI 使用边界**：可以用它解释概念、review 代码，但**每行代码必须能自己敲出来并讲清为什么**。答辩现场会让你加一个筛选条件，讲不出来就是没学会。
- **刻意留坑自测**：自己给自己埋——把 Redis 停掉看降级、把 MySQL 密码改错看报错、把锁 TTL 设成 1 秒看并发。这三件事做完，你对"生产环境"的理解会上一个台阶。

---

## 7. 风险与取舍

| 风险 | 应对 |
| --- | --- |
| Boot 4 的 starter 拆分导致依赖踩坑，一天出不来 | 给自己设 2 小时止损线，超时就用 `spring-boot-starter-data-redis` 的兼容写法或降级到 Boot 3.5 分支实验，主分支别动 |
| Docker 在 CentOS 7 上装不顺 | 先用 Windows + WSL2 或 Docker Desktop 跑通 compose，再迁到虚拟机；文档里注明"生产用 CentOS/Rocky 9" |
| Vue 三天做不完 | 砍掉规则配置页，保住"登录 → 列表 → 详情"闭环；方案原文也说前端目标是"看得懂、改得动" |
| 名单筛查算法陷进去 | 严格限定为"精确 + 归一化 + 编辑距离"，不做拼音、不做机器学习，一天收工 |
| 时间被学校事务打断 | 每块结束必须有一次 commit + 一段文档，保证"从最近一次答辩点恢复" |

---

## 8. 一句话总结

**你现在缺的不是新技术，而是把已经会的东西"做成可以被评审的样子"**：Redis 补上并发正确性，Docker 补上可复现交付，Vue 补齐路由与请求封装（渲染层你已经会了），业务补上名词和闭环，最后用一次答辩把它们串起来。三周之后，你手里就不是"一个跑通的项目"，而是"一段能讲清楚的工程经历"。
