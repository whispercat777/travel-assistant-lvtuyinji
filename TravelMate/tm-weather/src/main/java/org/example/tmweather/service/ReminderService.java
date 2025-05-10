package org.example.tmweather.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmweather.domain.po.Reminder;

import java.util.List;

public interface ReminderService extends IService<Reminder> {
    Integer addReminder(Reminder reminder);
    Integer modifyReminder(Reminder reminder);

    List<Reminder> getReminderByUserID(Integer userID);
}
