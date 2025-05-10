package org.example.tmfinance.client;

import org.example.tmfinance.domain.po.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "tm-plan")
public interface PlanClient {
    @GetMapping("/event/getall")
    public Result getAllEvent(@RequestParam("userID")Integer userID);
    @GetMapping("/event/getbyitiIDs")
    public Result getItiIDsEvent(@RequestParam("itiIDs")List<Integer> itiIDs) ;
}
