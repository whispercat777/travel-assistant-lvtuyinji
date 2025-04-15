package org.example.tmplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.pojo.Event;
import org.example.tmplan.pojo.Itinerary;
import org.example.tmplan.pojo.Result;
import org.example.tmplan.service.EventService;
import org.example.tmplan.service.IntRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class IntRecController {
    @Autowired
    private IntRecService intRecService;
    @GetMapping("/itinerary/getintrec")
    public Result getIntRec(Integer id) {
        log.info("getall intelligent recommendation");
        String answer=intRecService.getIntRec(id);
        return Result.success(answer);
    }
}
