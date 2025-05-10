package org.example.tmweather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.tmweather.mapper.ReminderMapper;
import org.example.tmweather.domain.po.Reminder;
import org.example.tmweather.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReminderServiceImpl extends ServiceImpl<ReminderMapper, Reminder> implements ReminderService {
    @Autowired
    private ReminderMapper reminderMapper;
    public Integer addReminder(Reminder reminder) {
        // 插入数据
        boolean isSaved = this.save(reminder);
        if (isSaved) {
            // 获取自动生成的 ID
            return reminder.getId();
        }
        return null; // 插入失败时返回 null 或可以抛出异常
    }
    public Integer modifyReminder(Reminder reminder) {
        // 创建 LambdaUpdateWrapper 来构建动态更新条件
        LambdaUpdateWrapper<Reminder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Reminder::getId, reminder.getId()) // 根据 ID 匹配
                .set(reminder.getTime() != null, Reminder::getTime, reminder.getTime()) // 如果 time 不为空，更新 time
                .set(reminder.getLocation() != null, Reminder::getLocation, reminder.getLocation()) // 如果 location 不为空，更新 location
                .set(reminder.getUserId() != null, Reminder::getUserId, reminder.getUserId()) // 如果 userId 不为空，更新 userId
                .set(reminder.getDate() != null, Reminder::getDate, reminder.getDate()); // 如果 data 不为空，更新 data

        // 执行更新操作
        int rows = reminderMapper.update(null, updateWrapper);
        if (rows > 0) {
            log.info("提醒ID为{}的记录更新成功", reminder.getId());
            return reminder.getId(); // 如果更新成功，返回记录 ID
        }

        log.warn("提醒ID为{}的记录更新失败", reminder.getId());
        return null; // 如果更新失败，返回 null
    }

    public List<Reminder> getReminderByUserID(Integer userID) {
        // 使用 MyBatis-Plus 提供的 LambdaQueryWrapper 来构建查询条件
        LambdaQueryWrapper<Reminder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reminder::getUserId, userID); // 根据用户 ID 匹配

        // 执行查询操作
        List<Reminder> reminders = reminderMapper.selectList(queryWrapper);
        return reminders;
    }
}
