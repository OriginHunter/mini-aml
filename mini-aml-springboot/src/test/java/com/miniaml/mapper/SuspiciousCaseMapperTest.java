package com.miniaml.mapper;

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
}
