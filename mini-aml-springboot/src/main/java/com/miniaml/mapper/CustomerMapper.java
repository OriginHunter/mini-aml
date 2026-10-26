package com.miniaml.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniaml.model.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    // 这里啥都不写！魔法都在 BaseMapper 里！
}