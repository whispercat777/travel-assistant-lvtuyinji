package org.example.tmweather.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    private float maxTemperature;
    private float minTemperature;
    private String description;
    private String wind;
    private LocalDate date;
    private String location;
}
