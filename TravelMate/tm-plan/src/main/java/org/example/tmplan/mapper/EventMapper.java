package org.example.tmplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.tmplan.pojo.Event;
import org.example.tmplan.pojo.Itinerary;

import java.util.List;

@Mapper
public interface EventMapper extends BaseMapper<Event> {
    @Select("SELECT * FROM event WHERE iti_id = #{itiID}")
    List<Event> findByItiID(Integer itiID);
}
