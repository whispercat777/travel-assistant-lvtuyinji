package org.example.tmfinance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.tmfinance.domain.po.Expense;
@Mapper
public interface ExpenseMapper extends BaseMapper<Expense> {
    // 查询特定 eveID 的记录数
    @Select("SELECT COUNT(*) FROM expense WHERE eve_id = #{eveID}")
    int countByEveID(@Param("eveID") Integer eveID);
}
