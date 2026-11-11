package com.miniaml.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniaml.common.Result;
import com.miniaml.dto.RuleUpdateRequest;
import com.miniaml.dto.SuspiciousCaseVO;
import com.miniaml.mapper.SuspiciousCaseMapper;
import com.miniaml.model.RuleConfig;
import com.miniaml.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;
    private final SuspiciousCaseMapper suspiciousCaseMapper;

    @PostMapping("/rules/{id}/execute")
    public Result<Integer> executeRule(@PathVariable Long id) {
        Integer integer = ruleService.executeRule(id);
        return Result.success(integer);
    }

    /** 查规则详情：走 Redis 缓存，命中时日志里看不到 SQL */
    @GetMapping("/rules/{id}")
    public Result<RuleConfig> getRule(@PathVariable Long id) {
        return Result.success(ruleService.getRuleConfig(id));
    }

    /** 改阈值 / 启停规则：先改库再删缓存，下一次读会自动写回新值 */
    @PutMapping("/rules/{id}")
    public Result<Void> updateRule(@PathVariable Long id, @RequestBody RuleUpdateRequest request) {
        ruleService.updateRuleConfig(id, request.threshold(), request.enabled());
        return Result.success(null);
    }

    @GetMapping("/cases/page")
    public Result<IPage<SuspiciousCaseVO>> getCasePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SuspiciousCaseVO> page = new Page<>(pageNum, pageSize);
        // 调用你写的分页联查
        IPage<SuspiciousCaseVO> result = suspiciousCaseMapper.selectCasePage(page, null, null, null, null);

        return Result.success(result);
    }

}
