package com.miniaml.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniaml.dto.DailyCaseReportVO;
import com.miniaml.dto.SuspiciousCaseVO;
import com.miniaml.model.SuspiciousCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SuspiciousCaseMapper extends BaseMapper<SuspiciousCase> {
    List<SuspiciousCaseVO> selectCaseListWithDetails(@Param("caseNo") String caseNo);
    IPage<SuspiciousCaseVO> selectCasePage(
            Page<SuspiciousCaseVO> page,
            @Param("caseNo") String caseNo,
            @Param("status") String status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    List<DailyCaseReportVO> selectDailyCaseReport();
}
