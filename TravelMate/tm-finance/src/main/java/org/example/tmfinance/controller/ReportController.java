package org.example.tmfinance.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.domain.po.TimeReport;
import org.example.tmfinance.domain.po.ItineraryReport;
import org.example.tmfinance.domain.po.TypeReport;
import org.example.tmfinance.service.BudgetService;
import org.example.tmfinance.service.ExpenseService;
import org.example.tmfinance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ReportController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ReportService reportService;
    @GetMapping("/report/getall")
    public Result getAllExpense(Integer userID) {
        TimeReport report =expenseService.getAllExpense(userID);
        return Result.success(report);
    }
    @GetMapping ("/report/type")
    public Result getTypeExpense(@RequestParam List<Integer> types, @RequestParam Integer userID) {
        TypeReport report =expenseService.getTypeExpense(types,userID);
        return Result.success(report);
    }
    @GetMapping ("/report/time")
    public Result getTimeExpense(String startTime, String endTime, Integer userID) {
        TimeReport report =expenseService.getTimeExpense(startTime,endTime,userID);
        return Result.success(report);
    }
    @GetMapping ("/report/budgetandexpense")
    public Result getAllBudgetAndExpense(Integer userID) {
        ItineraryReport report =reportService.getAllBudgetAndExpense(userID);
        return Result.success(report);
    }
    @GetMapping ("/report/iti")
    public Result getItiIDsBudgetAndExpense(@RequestParam List<Integer> itiIDs) {
        ItineraryReport report =reportService.getEventBudgetAndExpense(itiIDs);
        return Result.success(report);
    }
}
