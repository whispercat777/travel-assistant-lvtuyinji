package org.example.tmfinance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tmfinance.domain.po.Budget;

/**
 * 预算（Budget）表的数据访问接口。
 * 继承 BaseMapper，自动具备常规的增删改查操作方法。
 * 如需扩展自定义 SQL，可在此接口中添加方法并配合注解。
 */
@Mapper
public interface BudgetMapper extends BaseMapper<Budget> {

}
