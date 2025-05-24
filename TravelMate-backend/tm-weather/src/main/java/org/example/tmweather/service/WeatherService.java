package org.example.tmweather.service;

import org.example.tmweather.domain.po.Weather;

import java.time.LocalDate;

/**
 * 天气服务接口。
 * 定义获取天气信息的核心方法，用于根据地点和日期查询天气情况。
 */
public interface WeatherService {
    /**
     * 根据地点和日期获取天气信息。
     *
     * @param location 城市名称或城市编码（如 "上海" 或 "310000"）
     * @param date     查询的目标日期（如 LocalDate.now()）
     * @return 对应日期的天气信息封装对象 Weather，查询失败时可返回 null
     */
    Weather getWeather(String location, LocalDate date);
}
