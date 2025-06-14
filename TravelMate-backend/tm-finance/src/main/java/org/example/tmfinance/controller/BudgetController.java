package org.example.tmfinance.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmfinance.domain.po.Budget;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预算管理控制器，提供新增、修改、删除、查询预算的接口。
 * 所有接口路径以 /budget 开头，返回统一格式的 Result 对象。
 */
@Slf4j
@RestController
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    /**
     * 添加预算信息。
     * @param budget 预算实体对象
     * @return 操作结果，返回新增记录的主键 ID
     */
    @PostMapping("/budget/add")
    public Result addBudget(@RequestBody Budget budget) {
        Integer id = budgetService.addBudget(budget);
        return Result.success(id);
    }

    /**
     * 修改已有预算信息。
     * @param budget 预算实体对象，包含 ID
     * @return 操作结果，返回更新后的记录 ID
     */
    @PutMapping("/budget/modify")
    public Result modifyBudget(@RequestBody Budget budget) {
        Integer id = budgetService.modifyBudget(budget);
        return Result.success(id);
    }

    /**
     * 根据预算主键 ID 删除预算记录。
     * @param id 预算记录 ID
     * @return 操作结果，布尔值标识是否删除成功
     */
    @DeleteMapping("/budget/delete")
    public Result deleteBudget(Integer id) {
        boolean result = budgetService.removeById(id);
        return Result.success(result);
    }

    /**
     * 获取某事件下的所有预算记录。
     * @param eveID 事件 ID
     * @return 预算记录列表
     */
    @GetMapping("/budget/event")
    public Result getEventBudget(Integer eveID) {
        List<Budget> budgets = budgetService.getEventBudget(eveID);
        return Result.success(budgets);
    }

    /**
     * 删除某事件下的所有预算记录。
     * @param eveID 事件 ID
     * @return 操作结果，布尔值标识是否删除成功
     */
    @DeleteMapping("/budget/deletebyeveid")
    public Result deleteBudgetByEveID(Integer eveID) {
        boolean result = budgetService.deleteBudgetByEveID(eveID);
        return Result.success(result);
    }
}
