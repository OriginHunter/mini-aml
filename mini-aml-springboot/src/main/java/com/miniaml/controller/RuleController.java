package com.miniaml.controller;

import com.miniaml.common.Result;
import com.miniaml.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @PostMapping("/rules/{id}/execute")
    public Result<Integer> executeRule(@PathVariable Long id) {
        Integer integer = ruleService.executeRule(id);
        return Result.success(integer);
    }
}
