package org.example.tmweather.service;

import org.example.tmweather.domain.po.Weather;

import java.time.LocalDate;

public interface WeatherService {
    Weather getWeather(String location, LocalDate date);
}
