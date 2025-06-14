package org.example.tmfinance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.tmfinance.domain.po.Expense;

/**
 * 支出（Expense）表的数据访问接口。
 * 继承 MyBatis-Plus 提供的 BaseMapper，支持基本的增删改查操作。
 */
@Mapper
public interface ExpenseMapper extends BaseMapper<Expense> {

    /**
     * 统计某事件（eveID）下的支出记录数量。
     *
     * @param eveID 事件 ID
     * @return 支出记录数量
     */
    @Select("SELECT COUNT(*) FROM expense WHERE eve_id = #{eveID}")
    int countByEveID(@Param("eveID") Integer eveID);
}
