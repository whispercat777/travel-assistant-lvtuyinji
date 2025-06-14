package org.example.tmweather.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmweather.domain.po.Reminder;
import org.example.tmweather.domain.po.Result;
import org.example.tmweather.domain.po.Weather;
import org.example.tmweather.service.ReminderService;
import org.example.tmweather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 天气与提醒功能控制器。
 * 提供天气查询与用户提醒事项的增删改查接口，供前端调用。
 */
@Slf4j
@RestController
public class WeatherController {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private WeatherService weatherService;

    /**
     * 查询指定日期和地点的天气信息。
     *
     * @param location 查询位置（城市或地区）
     * @param date     查询日期
     * @return 指定日期的天气数据
     */
    @GetMapping("/weather/get")
    public Result getWeather(String location, LocalDate date) {
        Weather weather = weatherService.getWeather(location, date);
        return Result.success(weather);
    }

    /**
     * 添加新的提醒事项。
     *
     * @param reminder 提醒对象（包含时间、内容等信息）
     * @return 插入后的提醒 ID
     */
    @PostMapping("/reminder/add")
    public Result addReminder(@RequestBody Reminder reminder) {
        Integer id = reminderService.addReminder(reminder);
        return Result.success(id);
    }

    /**
     * 修改已有的提醒事项。
     *
     * @param reminder 修改后的提醒内容
     * @return 成功返回提醒 ID
     */
    @PutMapping("/reminder/modify")
    public Result modifyReminder(@RequestBody Reminder reminder) {
        Integer id = reminderService.modifyReminder(reminder);
        return Result.success(id);
    }

    /**
     * 删除指定提醒事项。
     *
     * @param id 提醒 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/reminder/delete")
    public Result deleteReminder(Integer id) {
        boolean result = reminderService.removeById(id);
        return Result.success(result);
    }

    /**
     * 根据用户 ID 获取其所有提醒事项。
     *
     * @param userID 用户 ID
     * @return 提醒事项列表
     */
    @GetMapping("/reminder/get")
    public Result getReminder(Integer userID) {
        List<Reminder> reminders = reminderService.getReminderByUserID(userID);
        return Result.success(reminders);
    }
}
