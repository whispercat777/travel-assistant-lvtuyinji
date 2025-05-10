package org.example.tmplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmplan.domain.po.Result;
import org.example.tmplan.service.IntRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IntRecController {
    @Autowired
    private IntRecService intRecService;
    @GetMapping("/itinerary/getintrec")
    public Result getIntRec(Integer id) {
        String answer=intRecService.getIntRec(id);
        return Result.success(answer);
    }
}
