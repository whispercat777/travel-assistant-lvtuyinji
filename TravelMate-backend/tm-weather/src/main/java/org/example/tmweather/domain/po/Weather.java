package org.example.tmweather.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 天气信息实体类。
 * 用于封装某一日期、地点的天气概况，通常从天气接口获取并返回给前端。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    /**
     * 当天的最高气温（单位：摄氏度）。
     */
    private float maxTemperature;

    /**
     * 当天的最低气温（单位：摄氏度）。
     */
    private float minTemperature;

    /**
     * 天气描述信息，如“晴”、“小雨”、“阴转多云”等。
     */
    private String description;

    /**
     * 风力信息，如“西南风3级”、“无持续风向微风”等。
     */
    private String wind;

    /**
     * 天气信息对应的日期。
     */
    private LocalDate date;

    /**
     * 天气对应的地理位置（城市/区域名）。
     */
    private String location;
}
