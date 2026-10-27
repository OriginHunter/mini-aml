package com.miniaml.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniaml.model.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    @Update("UPDATE customer SET deleted = 0 WHERE id = #{id}")
    int restoreCustomer(Long id);
}