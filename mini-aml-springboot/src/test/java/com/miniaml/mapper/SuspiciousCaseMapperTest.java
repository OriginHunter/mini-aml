package com.miniaml.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miniaml.dto.DailyCaseReportVO;
import com.miniaml.dto.SuspiciousCaseVO;
import com.miniaml.model.SuspiciousCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class SuspiciousCaseMapperTest {

    @Autowired
    private SuspiciousCaseMapper suspiciousCaseMapper;

    @Test
    public void testUseNull() {
        List<SuspiciousCaseVO> s = suspiciousCaseMapper.selectCaseListWithDetails(null);
        Assertions.assertNotEquals(0, s.size());
    }

    @Test
    public void testUseCaseNo() {
        List<SuspiciousCaseVO> s = suspiciousCaseMapper.selectCaseListWithDetails("AML20261102_64906498");
        Assertions.assertEquals(1, s.size());
    }

    @Test
    public void testSelectPage() {
        // 1. 创建分页对象：查第 1 页，每页 10 条
        Page<SuspiciousCaseVO> page = new Page<>(1, 10);

        // 2. 调用你在 Mapper 里写的分页方法（传 null 查全量）
        IPage<SuspiciousCaseVO> result = suspiciousCaseMapper.selectCasePage(page, null, null, null, null);

        // 3. 断言：当前页的数据量必须是 10 条
        Assertions.assertEquals(10, result.getRecords().size());

        // 4. 断言：总记录数必须是 9 万多条（用 > 90000 即可）
        Assertions.assertTrue(result.getTotal() > 90000);

        // 5. 顺手打印一下当前页的数据，看看封装结果对不对
        System.out.println("总条数：" + result.getTotal());
        System.out.println("当前页数据：" + result.getRecords());
    }

    @Test
    public void testDailyReport() {
        // 1. 调用你刚才写的按日统计方法
        List<DailyCaseReportVO> reportList = suspiciousCaseMapper.selectDailyCaseReport();

        // 2. 打印看看结果
        System.out.println("==== 每日案例统计报表 ====");
        for (DailyCaseReportVO vo : reportList) {
            System.out.println("日期: " + vo.getReportDate() + " | 数量: " + vo.getCaseCount());
        }

        // 3. 简单断言：报表肯定不能为空
        Assertions.assertNotNull(reportList);
        Assertions.assertNotEquals(0, reportList.size(), "统计报表查出来是空的！");
    }
}
