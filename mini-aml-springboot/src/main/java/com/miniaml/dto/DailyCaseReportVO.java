package com.miniaml.dto;

import lombok.Data;

@Data
public class DailyCaseReportVO {
    private String reportDate; // 日期，比如 "2026-11-03"
    private Integer caseCount; // 当天的案件总数
}
