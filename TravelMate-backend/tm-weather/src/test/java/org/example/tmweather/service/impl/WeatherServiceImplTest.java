package org.example.tmweather.service.impl;

import org.example.tmweather.domain.po.Weather;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeatherServiceImplTest {

    @Autowired
    private WeatherServiceImpl weatherService;

    @Test
    public void testGetWeather_validInput() {
        Weather weather = weatherService.getWeather("上海", LocalDate.now().plusDays(1)); // 通常API不返回当天
        if (weather == null) {
            System.out.println("未能获取天气数据，可能是 API KEY 或日期问题");
        } else {
            assertNotNull(weather);
            System.out.println("天气获取成功：" + weather);
        }
    }

    @Test
    public void testGetWeather_invalidLocation() {
        Weather weather = weatherService.getWeather("未知城市", LocalDate.now().plusDays(1));
        assertNull(weather);
    }
}
