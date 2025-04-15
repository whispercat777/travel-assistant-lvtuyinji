package org.example.tmplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.tmplan.pojo.Itinerary;

import java.util.List;

@Mapper
public interface ItineraryMapper extends BaseMapper<Itinerary> {
    @Select("SELECT * FROM itinerary WHERE user_id = #{userID}")
    List<Itinerary> findByUserID(Integer userID);
}
