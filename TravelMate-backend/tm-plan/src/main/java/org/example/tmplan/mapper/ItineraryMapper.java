package org.example.tmplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.tmplan.domain.po.Itinerary;

import java.util.List;

/**
 * 行程表（itinerary）对应的数据访问接口。
 * 继承 MyBatis-Plus 的 BaseMapper 提供通用 CRUD 操作。
 */
@Mapper
public interface ItineraryMapper extends BaseMapper<Itinerary> {

    /**
     * 根据用户 ID 查询其所有行程记录。
     *
     * @param userID 用户主键 ID
     * @return 该用户创建的行程列表
     */
    @Select("SELECT * FROM itinerary WHERE user_id = #{userID}")
    List<Itinerary> findByUserID(Integer userID);
}
