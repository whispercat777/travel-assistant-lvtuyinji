package org.example.tmplan.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.mapper.ItineraryMapper;
import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.dto.ItineraryDTO;
import org.example.tmplan.domain.vo.EventVo;
import org.example.tmplan.domain.vo.ItineraryVo;
import org.example.tmplan.service.EventService;
import org.example.tmplan.service.ItineraryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ItineraryServiceImpl extends ServiceImpl<ItineraryMapper, Itinerary> implements ItineraryService {
    @Autowired
    private ItineraryMapper itineraryMapper;
    @Autowired
    private EventService eventService;
    public Integer addItinerary(Itinerary itinerary) {
        // 插入数据
        boolean isSaved = this.save(itinerary);
        if (isSaved) {
            // 获取自动生成的 ID
            return itinerary.getID();
        }
        return null; // 插入失败时返回 null 或其他错误处理逻辑
    }
    // 根据 userID 获取所有行程
    public List<Itinerary> getItinerariesByUserID(Integer userID) {
        return itineraryMapper.findByUserID(userID);
    }
    public ItineraryVo getItineraryByID(Integer ID) {
        Itinerary itinerary=this.getById(ID);  // 根据 ID 查询行程
        ItineraryVo vo = new ItineraryVo();
        BeanUtils.copyProperties(itinerary, vo);
        vo.setEvents(eventService.findByItiID(ID));  // 查询行程下的所有事件
        return vo;
    }
    public Integer modifyItinerary(Itinerary itinerary) {
        // 创建 LambdaUpdateWrapper 来构建动态更新条件
        LambdaUpdateWrapper<Itinerary> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Itinerary::getID, itinerary.getID()) // 根据 ID 匹配
                .set(itinerary.getUserID() != null, Itinerary::getUserID, itinerary.getUserID()) // 如果 userID 不为空，更新 userID
                .set(itinerary.getName() != null, Itinerary::getName, itinerary.getName()) // 如果 name 不为空，更新 name
                .set(itinerary.getStartDate() != null, Itinerary::getStartDate, itinerary.getStartDate()) // 如果 startDate 不为空，更新 startDate
                .set(itinerary.getEndDate() != null, Itinerary::getEndDate, itinerary.getEndDate()) // 如果 endDate 不为空，更新 endDate
                .set(itinerary.getLocation() != null, Itinerary::getLocation, itinerary.getLocation()); // 如果 location 不为空，更新 location

        // 执行更新操作
        int rows = itineraryMapper.update(null, updateWrapper);
        if (rows > 0) {
            log.info("行程ID为{}的记录更新成功", itinerary.getID());
            return itinerary.getID(); // 如果更新成功，返回记录 ID
        }

        log.warn("行程ID为{}的记录更新失败", itinerary.getID());
        return null; // 如果更新失败，返回 null
    }

    @Override
    public boolean deleteItinerary(Integer id) {
        // 查询行程ID下的所有事件
        List<EventVo> events = eventService.findByItiID(id);
        if (events != null && !events.isEmpty()) {
            // 遍历删除每个事件
            for (EventVo event : events) {
                boolean eventDeleted = eventService.deleteEvent(event.getID());
                if (!eventDeleted) {
                    log.warn("删除事件ID为{}时失败", event.getID());
                    return false; // 如果有一个事件删除失败，则返回 false
                }
            }
        }

        // 删除行程
        boolean itineraryDeleted = this.removeById(id);
        if (itineraryDeleted) {
            log.info("行程ID为{}及其所有关联事件删除成功", id);
            return true; // 行程及所有事件成功删除
        }

        log.warn("删除行程ID为{}时失败", id);
        return false; // 如果行程删除失败
    }
    public ItineraryDTO getItineraryDetails(Integer id) {
        // 查询数据
        Itinerary itinerary = itineraryMapper.selectById(id);

        // 如果不存在，返回 null 或抛出异常
        if (itinerary == null) {
            throw new RuntimeException("Itinerary not found for ID: " + id);
        }

        // 组装结果
        return new ItineraryDTO(itinerary.getStartDate(), itinerary.getEndDate(), itinerary.getLocation());
    }

    public List<Integer> getEventByUserID(Integer userID) {
        // Step 1: Get all itineraries for the given user ID
        List<Itinerary> itineraries = itineraryMapper.findByUserID(userID);

        // Step 2: Initialize a list to hold the event IDs
        List<Integer> eventIDs = new ArrayList<>();

        // Step 3: For each itinerary, find its associated events and add their IDs to the list
        for (Itinerary itinerary : itineraries) {
            List<EventVo> events = eventService.findByItiID(itinerary.getID());
            for (EventVo event : events) {
                eventIDs.add(event.getID()); // Add event ID to the list
            }
        }

        // Step 4: Return the list of event IDs
        return eventIDs;
    }

}
