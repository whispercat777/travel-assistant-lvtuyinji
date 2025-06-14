package org.example.tmplan.service.impl;

import org.example.tmplan.domain.dto.ItineraryDTO;
import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.vo.EventVo;
import org.example.tmplan.mapper.ItineraryMapper;
import org.example.tmplan.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
@SpringBootTest
public class ItineraryServiceImplTest {

    @Mock
    private ItineraryMapper itineraryMapper;

    @Mock
    private EventService eventService;

    @InjectMocks
    private ItineraryServiceImpl itineraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(itineraryService, "baseMapper", itineraryMapper);
    }

    @Test
    void testAddItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setName("测试行程");

        when(itineraryMapper.insert(itinerary)).thenReturn(1);

        boolean saved = itineraryService.save(itinerary);
        assertThat(saved).isTrue();

        Integer id = itineraryService.addItinerary(itinerary);
        assertThat(id).isEqualTo(itinerary.getID());
    }

    @Test
    void testGetItinerariesByUserID() {
        Itinerary itinerary = new Itinerary();
        itinerary.setUserID(123);

        when(itineraryMapper.findByUserID(123)).thenReturn(Collections.singletonList(itinerary));

        List<Itinerary> result = itineraryService.getItinerariesByUserID(123);
        assertThat(result).isNotEmpty();
    }

    @Test
    void testGetItineraryByID() {
        Itinerary itinerary = new Itinerary();
        itinerary.setID(1);

        when(itineraryMapper.selectById(1)).thenReturn(itinerary);
        when(eventService.findByItiID(1)).thenReturn(Collections.emptyList());

        ItineraryDTO result = itineraryService.getItineraryDetails(1);
        assertThat(result).isNotNull();
    }

    @Test
    void testModifyItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setID(1);
        itinerary.setName("修改后的行程");

        when(itineraryMapper.update(any(), any())).thenReturn(1);

        Integer id = itineraryService.modifyItinerary(itinerary);
        assertThat(id).isEqualTo(1);
    }

    @Test
    void testDeleteItinerary() {
        EventVo event = new EventVo();
        event.setID(1);

        when(eventService.findByItiID(1)).thenReturn(Collections.singletonList(event));
        when(eventService.deleteEvent(1)).thenReturn(true);
        when(itineraryMapper.deleteById(1)).thenReturn(1);

        boolean deleted = itineraryService.deleteItinerary(1);
        assertThat(deleted).isTrue();
    }

    @Test
    void testGetEventByUserID() {
        Itinerary itinerary = new Itinerary();
        itinerary.setID(1);
        itinerary.setUserID(123);

        EventVo eventVo = new EventVo();
        eventVo.setID(10);

        when(itineraryMapper.findByUserID(123)).thenReturn(Collections.singletonList(itinerary));
        when(eventService.findByItiID(1)).thenReturn(Collections.singletonList(eventVo));

        List<Integer> eventIds = itineraryService.getEventByUserID(123);
        assertThat(eventIds).containsExactly(10);
    }

    @Test
    void testGetItineraryDetailsNotFound() {
        when(itineraryMapper.selectById(999)).thenReturn(null);

        try {
            itineraryService.getItineraryDetails(999);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Itinerary not found for ID: 999");
        }
    }
}
