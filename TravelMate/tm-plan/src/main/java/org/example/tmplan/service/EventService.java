package org.example.tmplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmplan.domain.po.Event;
import org.example.tmplan.domain.vo.EventVo;

import java.util.List;

public interface EventService extends IService<Event> {
    List<EventVo> findByItiID(Integer itiID);
    Integer addEvent(Event event);
    Integer modifyEvent(Event event);
    boolean deleteEvent(Integer id);
    List<Integer> getEventByItiIDs(List<Integer> itiIDs);
}
