package org.example.tmplan.client;

import org.example.tmplan.domain.po.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tm-finance")
public interface FinanceClient {
    @GetMapping("/budget/event")
    Result getEventBudget(@RequestParam("eveID") Integer eveID);

    @GetMapping("/expense/event")
    Result getEventExpense(@RequestParam("eveID") Integer eveID);

    @DeleteMapping ("/expense/deletebyeveid")
    Result deleteExpenseByEveID(@RequestParam("eveID")Integer eveID);

    @DeleteMapping ("/budget/deletebyeveid")
    public Result deleteBudgetByEveID(@RequestParam("eveID")Integer eveID);
}

