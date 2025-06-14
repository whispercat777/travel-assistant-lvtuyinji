package org.example.tmplan.service.impl;

import org.example.tmplan.client.FinanceClient;
import org.example.tmplan.domain.po.Event;
import org.example.tmplan.domain.po.Result;
import org.example.tmplan.domain.vo.Budget;
import org.example.tmplan.domain.vo.Expense;
import org.example.tmplan.mapper.EventMapper;
import org.example.tmplan.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
@SpringBootTest
public class EventServiceImplTest {

    @Mock
    private EventMapper eventMapper;

    @Mock
    private FinanceClient financeClient;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 注入 baseMapper，否则 MyBatis-Plus 方法会抛异常
        ReflectionTestUtils.setField(eventService, "baseMapper", eventMapper);
    }

    @Test
    void testAddEvent() {
        Event event = new Event();
        event.setName("测试事件");

        when(eventMapper.insert(event)).thenReturn(1);

        boolean saved = eventService.save(event);
        assertThat(saved).isTrue();

        Integer id = eventService.addEvent(event);
        assertThat(id).isEqualTo(event.getID());
    }

    @Test
    void testFindByItiID() {
        Event event = new Event();
        event.setID(1);
        event.setItiID(10);
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(1));
        event.setLocation("测试地点");
        event.setName("测试事件");

        List<Event> eventList = Collections.singletonList(event);

        when(eventMapper.findByItiID(10)).thenReturn(eventList);
        when(financeClient.getEventBudget(1)).thenReturn(new Result(1, "success", new ArrayList<Budget>()));
        when(financeClient.getEventExpense(1)).thenReturn(new Result(1, "success", new ArrayList<Expense>()));

        List<?> result = eventService.findByItiID(10);
        assertThat(result).isNotEmpty();
    }

    @Test
    void testModifyEvent() {
        Event event = new Event();
        event.setID(1);
        event.setName("修改后的名称");

        when(eventMapper.update(any(), any())).thenReturn(1);

        Integer id = eventService.modifyEvent(event);
        assertThat(id).isEqualTo(1);
    }

    @Test
    void testDeleteEvent() {
        when(financeClient.deleteBudgetByEveID(1)).thenReturn(new Result(1, "success", null));
        when(financeClient.deleteExpenseByEveID(1)).thenReturn(new Result(1, "success", null));
        when(eventMapper.deleteById(1)).thenReturn(1);

        boolean deleted = eventService.deleteEvent(1);
        assertThat(deleted).isTrue();
    }

    @Test
    void testGetEventByItiIDs() {
        Event event1 = new Event();
        event1.setID(1);
        event1.setItiID(100);

        List<Event> events = Collections.singletonList(event1);

        when(eventMapper.selectList(any())).thenReturn(events);

        List<Integer> eventIds = eventService.getEventByItiIDs(Collections.singletonList(100));
        assertThat(eventIds).containsExactly(1);
    }

    @Test
    void testGetEventByItiIDsWhenEmpty() {
        List<Integer> result = eventService.getEventByItiIDs(Collections.emptyList());
        assertThat(result).isEmpty();
    }
}
