package com.miniaml.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniaml.dto.SuspiciousCaseVO;
import com.miniaml.model.SuspiciousCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SuspiciousCaseMapper extends BaseMapper<SuspiciousCase> {
    List<SuspiciousCaseVO> selectCaseListWithDetails(@Param("caseNo") String caseNo);
}
