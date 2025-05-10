package org.example.tmfinance.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmfinance.domain.po.Budget;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BudgetController {
    @Autowired
    private BudgetService budgetService;
    @PostMapping("/budget/add")
    public Result addBudget(@RequestBody Budget budget) {
        Integer id=budgetService.addBudget(budget);
        return Result.success(id);
    }
    @PutMapping("/budget/modify")
    public Result modifyBudget(@RequestBody Budget budget) {
        Integer id = budgetService.modifyBudget(budget);
        return Result.success(id);
    }
    @DeleteMapping("/budget/delete")
    public Result deleteBudget(Integer id) {
        boolean result=budgetService.removeById(id);
        return Result.success(result);
    }
    @GetMapping ("/budget/event")
    public Result getEventBudget(Integer eveID) {
        List<Budget> budgets =budgetService.getEventBudget(eveID);
        return Result.success(budgets);
    }
    @DeleteMapping ("/budget/deletebyeveid")
    public Result deleteBudgetByEveID(Integer eveID) {
        boolean result=budgetService.deleteBudgetByEveID(eveID);
        return Result.success(result);
    }
}
