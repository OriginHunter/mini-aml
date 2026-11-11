package com.miniaml.service;

import com.miniaml.model.RuleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

/**
 * 规则配置的 Redis 缓存（Cache-Aside 模式）。
 *
 * <p>为什么缓存规则配置：跑批是读多写少的场景，规则表一次跑批要读好几次，
 * 而规则可能几个月才改一次，没必要每次都打到 MySQL。
 *
 * <p>为什么用 StringRedisTemplate 而不是 RedisTemplate&lt;String, Object&gt;：
 * 后者要自己配序列化器，默认的 JDK 序列化在 redis-cli 里看着是一堆乱码，
 * 出问题时没法排查。这里统一存 JSON 文本，redis-cli 里 GET 一下就能看懂。
 *
 * <p>为什么更新时是“删缓存”而不是“更新缓存”：
 * 两个线程同时改配置，A 先改库、B 后改库、但 B 先写缓存、A 后写缓存，
 * 缓存里就会留下 A 的旧值且一直不过期。删除是幂等的，下次读自然回源，最安全。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RuleCacheService {

    /** 所有规则缓存的统一前缀，方便 redis-cli KEYS 'aml:rule:*' 排查与批量清理 */
    public static final String KEY_PREFIX = "aml:rule:";

    /** 30 分钟：配置类数据不需要强一致，给个兜底过期时间防止脏数据永久驻留 */
    public static final Duration TTL = Duration.ofMinutes(30);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /** 读缓存；未命中或 Redis 不可用时返回 empty，由调用方回源查库 */
    public Optional<RuleConfig> get(Long ruleId) {
        if (ruleId == null) {
            return Optional.empty();
        }
        String key = KEY_PREFIX + ruleId;
        String json;
        try {
            json = redisTemplate.opsForValue().get(key);
        } catch (RuntimeException e) {
            // 缓存是“加速器”不是“必需品”：Redis 挂了业务必须还能跑，只是慢一点
            log.warn("读取 Redis 失败，降级回源，key={}", key, e);
            return Optional.empty();
        }
        if (json == null) {
            log.debug("缓存未命中，key={}", key);
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, RuleConfig.class));
        } catch (JacksonException e) {
            // 类结构改过 / 脏数据：删掉重来，别让它一直挡在缓存里导致每次反序列化都失败
            log.warn("缓存反序列化失败，删除脏 key={}", key, e);
            evict(ruleId);
            return Optional.empty();
        }
    }

    /** 回源查库后写回缓存 */
    public void put(RuleConfig ruleConfig) {
        if (ruleConfig == null || ruleConfig.getId() == null) {
            return;
        }
        String key = KEY_PREFIX + ruleConfig.getId();
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(ruleConfig), TTL);
            log.debug("写入缓存 key={}，TTL={} 分钟", key, TTL.toMinutes());
        } catch (RuntimeException e) {
            log.warn("写入 Redis 失败，不影响本次请求，key={}", key, e);
        }
    }

    /** 配置变更后调用：先改库，再删缓存 */
    public void evict(Long ruleId) {
        if (ruleId == null) {
            return;
        }
        String key = KEY_PREFIX + ruleId;
        try {
            redisTemplate.delete(key);
            log.info("删除规则缓存 key={}", key);
        } catch (RuntimeException e) {
            log.warn("删除 Redis key 失败，key={}", key, e);
        }
    }
}
