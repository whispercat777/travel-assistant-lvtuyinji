package org.example.tmplan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.mapper.EventMapper;
import org.example.tmplan.pojo.Event;
import org.example.tmplan.pojo.vo.EventVo;
import org.example.tmplan.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {
    @Autowired
    private EventMapper eventMapper;
    public Integer addEvent(Event event) {
        // 插入数据
        boolean isSaved = this.save(event);
        if (isSaved) {
            // 获取自动生成的 ID
            return event.getID();
        }
        return null; // 插入失败时返回 null 或其他错误处理逻辑
    }
    public List<EventVo> findByItiID(Integer itiID) {
        log.info("查询行程ID为{}的所有事件", itiID);

        // 查询所有事件
        List<Event> events = eventMapper.findByItiID(itiID);

        // 创建 EventVo 列表
        List<EventVo> eventVos = new ArrayList<>();

        for (Event event : events) {
            // 创建 EventVo 对象
            EventVo vo = new EventVo();

            // 复制属性
            BeanUtils.copyProperties(event, vo);


        }

        return eventVos;
    }

    public Integer modifyEvent(Event event) {
        // 创建 LambdaUpdateWrapper 来构建动态更新条件
        LambdaUpdateWrapper<Event> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Event::getID, event.getID()) // 根据 ID 匹配
                .set(event.getItiID() != null, Event::getItiID, event.getItiID()) // 如果 itiID 不为空，更新 itiID
                .set(event.getStartTime() != null, Event::getStartTime, event.getStartTime()) // 如果 startTime 不为空，更新 startTime
                .set(event.getEndTime() != null, Event::getEndTime, event.getEndTime()) // 如果 endTime 不为空，更新 endTime
                .set(event.getLocation() != null, Event::getLocation, event.getLocation()) // 如果 location 不为空，更新 location
                .set(event.getDescription() != null, Event::getDescription, event.getDescription()) // 如果 description 不为空，更新 description
                .set(event.getName() != null, Event::getName, event.getName()) // 如果 name 不为空，更新 name
                .set(event.getType() != null, Event::getType, event.getType()); // 如果 type 不为空，更新 type

        // 执行更新操作
        int rows = eventMapper.update(null, updateWrapper);
        if (rows > 0) {
            log.info("事件ID为{}的记录更新成功", event.getID());
            return event.getID(); // 如果更新成功，返回记录 ID
        }

        log.warn("事件ID为{}的记录更新失败", event.getID());
        return null; // 如果更新失败，返回 null
    }

    public boolean deleteEvent(Integer id){
        // 删除事件
        boolean isDeleted = this.removeById(id);
        if(isDeleted){
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getEventByItiIDs(List<Integer> itiIDs) {
        // 检查输入的行程 ID 列表是否为空
        if (itiIDs == null || itiIDs.isEmpty()) {
            return new ArrayList<>(); // 如果为空，返回空列表
        }

        // 创建查询条件
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Event::getItiID, itiIDs); // 条件：itiID 在指定的列表中

        // 只查询 ID 字段
        List<Event> events = this.list(queryWrapper);

        // 提取事件 ID 列表
        List<Integer> eventIDs = new ArrayList<>();
        for (Event event : events) {
            eventIDs.add(event.getID());
        }

        return eventIDs; // 返回事件 ID 列表
    }

}
