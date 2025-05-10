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

@Slf4j
@RestController
public class WeatherController {
    @Autowired
    private ReminderService reminderService;
    @Autowired
    private WeatherService weatherService;
    @GetMapping("/weather/get")
    public Result getWeather(String location, LocalDate date) {
        Weather weather = weatherService.getWeather(location, date);
        return Result.success(weather);
    }

    @PostMapping("/reminder/add")
    public Result addReminder(@RequestBody Reminder reminder) {
        Integer id = reminderService.addReminder(reminder);
        return Result.success(id);
    }
    @PutMapping("/reminder/modify")
    public Result modifyReminder(@RequestBody Reminder reminder) {
        Integer id =reminderService.modifyReminder(reminder);
        return Result.success(id);
    }
    @DeleteMapping("/reminder/delete")
    public Result deleteReminder(Integer id) {
        boolean result=reminderService.removeById(id);
        return Result.success(result);
    }
    @GetMapping("/reminder/get")
    public Result getReminder(Integer userID) {
        List<Reminder> reminders = reminderService.getReminderByUserID(userID);
        return Result.success(reminders);
    }
}
