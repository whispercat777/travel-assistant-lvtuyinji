package org.example.tmweather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tmweather.domain.po.Reminder;

/**
 * ReminderMapper 接口。
 * 继承 MyBatis-Plus 提供的 BaseMapper，用于操作 reminder 表的基本 CRUD 功能。
 * 通过泛型指定实体类为 Reminder，无需手写 XML 即可执行增删改查操作。
 */
@Mapper
public interface ReminderMapper extends BaseMapper<Reminder> {
    // 如需扩展自定义 SQL，可在此定义方法并结合 @Select、@Update 等注解或 XML 映射文件
}
