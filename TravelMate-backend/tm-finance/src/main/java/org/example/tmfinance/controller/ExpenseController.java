package org.example.tmfinance.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支出管理控制器，提供支出记录的增删改查等功能。
 * 所有接口以 /expense 开头，统一返回 Result 封装结果。
 */
@Slf4j
@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    /**
     * 添加一条新的支出记录。
     * @param expense 支出对象
     * @return 操作结果，返回新添加记录的主键 ID
     */
    @PostMapping("/expense/add")
    public Result addExpense(@RequestBody Expense expense) {
        Integer id = expenseService.addExpense(expense);
        return Result.success(id);
    }

    /**
     * 根据事件 ID 统计支出记录数。
     * @param eveID 事件 ID
     * @return 操作结果，返回记录条数
     */
    @GetMapping("/expense/eventID/number")
    public Result countRecordsByEveID(Integer eveID) {
        Integer count = expenseService.countRecordsByEveID(eveID);
        return Result.success(count);
    }

    /**
     * 修改已有的支出记录。
     * @param expense 支出对象，需包含主键 ID
     * @return 操作结果，返回修改的记录 ID
     */
    @PutMapping("/expense/modify")
    public Result modifyExpense(@RequestBody Expense expense) {
        Integer id = expenseService.modifyExpense(expense);
        return Result.success(id);
    }

    /**
     * 根据支出记录主键 ID 删除记录。
     * @param id 支出记录 ID
     * @return 操作结果，布尔值标识是否成功删除
     */
    @DeleteMapping("/expense/delete")
    public Result deleteExpense(Integer id) {
        boolean result = expenseService.removeById(id);
        return Result.success(result);
    }

    /**
     * 查询某事件下的所有支出记录。
     * @param eveID 事件 ID
     * @return 操作结果，返回支出记录列表
     */
    @GetMapping("/expense/event")
    public Result getEventExpense(Integer eveID) {
        List<Expense> expenses = expenseService.getEventExpense(eveID);
        return Result.success(expenses);
    }

    /**
     * 删除某事件下的所有支出记录。
     * @param eveID 事件 ID
     * @return 操作结果，布尔值标识是否成功删除
     */
    @DeleteMapping("/expense/deletebyeveid")
    public Result deleteExpenseByEveID(Integer eveID) {
        boolean result = expenseService.deleteExpenseByEveID(eveID);
        return Result.success(result);
    }
}
