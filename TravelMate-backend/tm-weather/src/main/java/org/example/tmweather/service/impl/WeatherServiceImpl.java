package org.example.tmweather.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.tmweather.domain.po.Weather;
import org.example.tmweather.service.WeatherService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherServiceImpl implements WeatherService {

    public Weather getWeather(String location, LocalDate date) {
        String API_URL = "https://restapi.amap.com/v3/weather/weatherInfo";
        String API_KEY = "d956654a9b90181094c0636f7888f326"; // 替换为你的 API Key

        try {
            // 格式化日期为字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 构建请求 URL
            String urlString = String.format(
                    "%s?key=%s&city=%s&extensions=all&output=JSON",
                    API_URL, API_KEY, location
            );

            // 发起 HTTP GET 请求
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 检查响应状态
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed. Code: " + connection.getResponseCode());
            }

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // 解析 JSON 响应
            JSONObject jsonResponse = JSON.parseObject(responseBuilder.toString());
            if (!"1".equals(jsonResponse.getString("status"))) {
                throw new RuntimeException("API Error: " + jsonResponse.getString("info"));
            }

            // 查找指定日期的天气预报
            JSONArray forecasts = jsonResponse.getJSONArray("forecasts");
            if (forecasts.isEmpty()) {
                throw new RuntimeException("No weather data available for the location.");
            }
            JSONArray casts = forecasts.getJSONObject(0).getJSONArray("casts");
            for (int i = 0; i < casts.size(); i++) {
                JSONObject forecast = casts.getJSONObject(i);
                if (forecast.getString("date").equals(formatter.format(date))) {
                    // 构造 Weather 对象
                    return new Weather(
                            Float.parseFloat(forecast.getString("daytemp")),
                            Float.parseFloat(forecast.getString("nighttemp")),
                            forecast.getString("dayweather"),
                            forecast.getString("daypower"), // 使用风力描述
                            date,
                            location
                    );
                }
            }

            throw new RuntimeException("Weather data for the specified date not found.");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}