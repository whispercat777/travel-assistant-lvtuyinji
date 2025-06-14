package org.example.tmplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.tmplan.domain.po.Event;

import java.util.List;

/**
 * 事件表（event）对应的数据库访问接口。
 * 继承 MyBatis-Plus 提供的 BaseMapper，可自动实现常规增删改查操作。
 */
@Mapper
public interface EventMapper extends BaseMapper<Event> {

    /**
     * 根据行程 ID 查询该行程下的所有事件。
     *
     * @param itiID 行程主键 ID
     * @return 属于该行程的事件列表
     */
    @Select("SELECT * FROM event WHERE iti_id = #{itiID}")
    List<Event> findByItiID(Integer itiID);
}
