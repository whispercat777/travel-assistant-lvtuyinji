package org.example.tmplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.domain.po.Event;
import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.po.Result;
import org.example.tmplan.domain.vo.ItineraryVo;
import org.example.tmplan.service.EventService;
import org.example.tmplan.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ItineraryController {
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private EventService eventService;
    @PostMapping("/itinerary/add")
    public Result addItinerary(@RequestBody Itinerary itinerary) {
        Integer id = itineraryService.addItinerary(itinerary);
        return Result.success(id);
    }
    @GetMapping("/itinerary/getall")
    public Result getAllItinerary(Integer userID) {
        List<Itinerary> itineraries = itineraryService.getItinerariesByUserID(userID);
        return Result.success(itineraries);
    }
    @GetMapping("/itinerary/getone")
    public Result getOneItinerary(Integer ID) {
        ItineraryVo vo = itineraryService.getItineraryByID(ID);
        return Result.success(vo);
    }
    @PostMapping("/event/add")
    public Result addEvent(@RequestBody Event event) {
        log.info("add event");
        Integer id = eventService.addEvent(event);
        log.info("id={}", id);
        return Result.success(id);
    }

    @PutMapping("/event/modify")
    public Result modifyEvent(@RequestBody Event event) {
        Integer id = eventService.modifyEvent(event);
        return Result.success(id);
    }
    @DeleteMapping("/event/delete")
    public Result deleteEvent(Integer id) {
        boolean result=eventService.deleteEvent(id);
        return Result.success(result);
    }

    @PutMapping("/itinerary/modify")
    public Result modifyItinerary(@RequestBody Itinerary itinerary) {
        Integer id =itineraryService.modifyItinerary(itinerary);
        return Result.success(id);
    }
    @DeleteMapping("/itinerary/delete")
    public Result deleteItinerary(Integer id) {
        boolean result=itineraryService.deleteItinerary(id);
        return Result.success(result);
    }
    @GetMapping("/event/getall")
    public Result getAllEvent(Integer userID) {
        List<Integer> ids = itineraryService.getEventByUserID(userID);
        return Result.success(ids);
    }
    @GetMapping("/event/getbyitiIDs")
    public Result getItiIDsEvent(@RequestParam List<Integer> itiIDs) {
        List<Integer> ids = eventService.getEventByItiIDs(itiIDs);
        return Result.success(ids);
    }


}
