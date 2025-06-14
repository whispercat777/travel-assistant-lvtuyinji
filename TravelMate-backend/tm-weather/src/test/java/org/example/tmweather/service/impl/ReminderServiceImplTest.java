package org.example.tmweather.service.impl;


import org.example.tmweather.domain.po.Reminder;
import org.example.tmweather.mapper.ReminderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.mock.mockito.SpyBean;



@MapperScan("org.example.tmweather.mapper")
@SpringBootTest
public class ReminderServiceImplTest {

    @SpyBean
    private ReminderServiceImpl reminderService;

    @MockBean
    private ReminderMapper reminderMapper;

    private Reminder sampleReminder;

    @BeforeEach
    public void setup() {
        sampleReminder = new Reminder(
                1,
                LocalDateTime.of(2025, 5, 13, 10, 0),
                "Beijing",
                123,
                LocalDate.of(2025, 5, 15)
        );
    }

    @Test
    public void testAddReminder_success() {
        doReturn(true).when(reminderService).save(sampleReminder);
        Integer result = reminderService.addReminder(sampleReminder);
        assertEquals(1, result);
    }

    @Test
    public void testAddReminder_failure() {
        doReturn(false).when(reminderService).save(sampleReminder);
        Integer result = reminderService.addReminder(sampleReminder);
        assertNull(result);
    }

    @Test
    public void testModifyReminder_success() {
        when(reminderMapper.update(any(), any())).thenReturn(1);
        Integer result = reminderService.modifyReminder(sampleReminder);
        assertEquals(1, result);
    }

    @Test
    public void testModifyReminder_failure() {
        when(reminderMapper.update(any(), any())).thenReturn(0);
        Integer result = reminderService.modifyReminder(sampleReminder);
        assertNull(result);
    }

    @Test
    public void testGetReminderByUserID() {
        when(reminderMapper.selectList(any())).thenReturn(List.of(sampleReminder));
        List<Reminder> result = reminderService.getReminderByUserID(123);
        assertEquals(1, result.size());
        assertEquals(123, result.get(0).getUserId());
    }
}
