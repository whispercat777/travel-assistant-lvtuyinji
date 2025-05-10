package org.example.tmfinance.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @PostMapping("/expense/add")
    public Result addExpense(@RequestBody Expense expense) {
        Integer id = expenseService.addExpense(expense);
        return Result.success(id);
    }
    @GetMapping("/expense/eventID/number")
    public Result countRecordsByEveID(Integer eveID) {
        Integer count = expenseService.countRecordsByEveID(eveID);
        return Result.success(count);
    }
    @PutMapping ("/expense/modify")
    public Result modifyExpense(@RequestBody Expense expense) {
        Integer id = expenseService.modifyExpense(expense);
        return Result.success(id);
    }
    @DeleteMapping ("/expense/delete")
    public Result deleteExpense(Integer id) {
        boolean result=expenseService.removeById(id);
        return Result.success(result);
    }
    @GetMapping ("/expense/event")
    public Result getEventExpense(Integer eveID) {
        List<Expense> expenses =expenseService.getEventExpense(eveID);
        return Result.success(expenses);
    }
    @DeleteMapping ("/expense/deletebyeveid")
    public Result deleteExpenseByEveID(Integer eveID) {
        boolean result=expenseService.deleteExpenseByEveID(eveID);
        return Result.success(result);
    }


}
