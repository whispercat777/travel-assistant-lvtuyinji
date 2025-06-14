package org.example.tmweather.controller;

import org.example.tmweather.domain.po.Reminder;
import org.example.tmweather.domain.po.Result;
import org.example.tmweather.domain.po.Weather;
import org.example.tmweather.service.ReminderService;
import org.example.tmweather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeatherControllerTest {

    @Autowired
    private WeatherController weatherController;

    @MockBean
    private ReminderService reminderService;

    @MockBean
    private WeatherService weatherService;

    private Reminder sampleReminder;
    private Weather sampleWeather;

    @BeforeEach
    public void setUp() {
        sampleReminder = new Reminder(1, LocalDateTime.now(), "Shanghai", 101, LocalDate.now());
        sampleWeather = new Weather(30.5f, 22.3f, "Sunny", "East Wind", LocalDate.now(), "Shanghai");
    }

    @Test
    public void testGetWeather() {
        when(weatherService.getWeather("Shanghai", LocalDate.now())).thenReturn(sampleWeather);

        Result result = weatherController.getWeather("Shanghai", LocalDate.now());
        assertEquals(1, result.getCode());
        assertNotNull(result.getData());
        assertTrue(result.getData() instanceof Weather);
    }

    @Test
    public void testAddReminder() {
        when(reminderService.addReminder(sampleReminder)).thenReturn(1);

        Result result = weatherController.addReminder(sampleReminder);
        assertEquals(1, result.getCode());
        assertEquals(1, result.getData());
    }

    @Test
    public void testModifyReminder() {
        when(reminderService.modifyReminder(sampleReminder)).thenReturn(1);

        Result result = weatherController.modifyReminder(sampleReminder);
        assertEquals(1, result.getCode());
        assertEquals(1, result.getData());
    }

    @Test
    public void testDeleteReminder() {
        when(reminderService.removeById(1)).thenReturn(true);

        Result result = weatherController.deleteReminder(1);
        assertEquals(1, result.getCode());
        assertEquals(true, result.getData());
    }

    @Test
    public void testGetReminder() {
        List<Reminder> reminders = Arrays.asList(sampleReminder);
        when(reminderService.getReminderByUserID(101)).thenReturn(reminders);

        Result result = weatherController.getReminder(101);
        assertEquals(1, result.getCode());
        assertNotNull(result.getData());
        assertTrue(result.getData() instanceof List);
    }
}
