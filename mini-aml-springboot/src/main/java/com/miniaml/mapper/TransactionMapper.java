package com.miniaml.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miniaml.dto.TransactionDetailDTO;
import com.miniaml.model.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

    // 这里不需要贴 @Select 注解了，它会自动去找 XML 里 id 叫 findTransactionDetails 的 SQL
    List<TransactionDetailDTO> findTransactionDetails(@Param("type") String type);
}
