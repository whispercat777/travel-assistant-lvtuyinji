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

/**
 * 提醒服务实现类。
 * 实现 ReminderService 接口，提供添加、修改、查询用户提醒的具体逻辑。
 * 继承 MyBatis-Plus 的 ServiceImpl，封装基础 CRUD 方法。
 */
@Slf4j
@Service
public class ReminderServiceImpl extends ServiceImpl<ReminderMapper, Reminder> implements ReminderService {

    @Autowired
    private ReminderMapper reminderMapper;

    /**
     * 添加新的提醒记录。
     *
     * @param reminder 提醒对象
     * @return 插入成功返回自动生成的提醒 ID，失败返回 null
     */
    @Override
    public Integer addReminder(Reminder reminder) {
        boolean isSaved = this.save(reminder);
        if (isSaved) {
            return reminder.getId();
        }
        return null;
    }

    /**
     * 修改已有的提醒记录（仅更新非空字段）。
     *
     * @param reminder 提醒修改内容
     * @return 更新成功返回提醒 ID，失败返回 null
     */
    @Override
    public Integer modifyReminder(Reminder reminder) {
        LambdaUpdateWrapper<Reminder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Reminder::getId, reminder.getId())
                .set(reminder.getTime() != null, Reminder::getTime, reminder.getTime())
                .set(reminder.getLocation() != null, Reminder::getLocation, reminder.getLocation())
                .set(reminder.getUserId() != null, Reminder::getUserId, reminder.getUserId())
                .set(reminder.getDate() != null, Reminder::getDate, reminder.getDate());

        int rows = reminderMapper.update(null, updateWrapper);
        if (rows > 0) {
            log.info("提醒ID为{}的记录更新成功", reminder.getId());
            return reminder.getId();
        }

        log.warn("提醒ID为{}的记录更新失败", reminder.getId());
        return null;
    }

    /**
     * 根据用户 ID 查询该用户的所有提醒记录。
     *
     * @param userID 用户主键 ID
     * @return 提醒记录列表
     */
    @Override
    public List<Reminder> getReminderByUserID(Integer userID) {
        LambdaQueryWrapper<Reminder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reminder::getUserId, userID);
        return reminderMapper.selectList(queryWrapper);
    }
}
