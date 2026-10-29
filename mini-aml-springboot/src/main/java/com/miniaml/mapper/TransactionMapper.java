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

    List<Transaction> getBySortField(@Param("sortField") String sortField);

    // 传入一个 ID 的 List，查出这些 ID 对应的交易
    List<Transaction> findByIds(@Param("ids") List<Long> ids);
}
