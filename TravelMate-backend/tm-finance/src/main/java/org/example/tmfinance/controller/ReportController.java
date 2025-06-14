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

/**
 * 报表控制器，提供对用户支出、预算的统计与分析结果接口。
 * 支持按时间、类型、行程等维度进行支出预算汇总。
 */
@Slf4j
@RestController
public class ReportController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ReportService reportService;

    /**
     * 获取某用户所有时间维度的支出总览。
     * @param userID 用户 ID
     * @return 时间汇总报告对象
     */
    @GetMapping("/report/getall")
    public Result getAllExpense(Integer userID) {
        TimeReport report = expenseService.getAllExpense(userID);
        return Result.success(report);
    }

    /**
     * 获取用户在指定支出类型下的支出总览。
     * @param types 支出类型 ID 列表
     * @param userID 用户 ID
     * @return 类型汇总报告对象
     */
    @GetMapping("/report/type")
    public Result getTypeExpense(@RequestParam List<Integer> types, @RequestParam Integer userID) {
        TypeReport report = expenseService.getTypeExpense(types, userID);
        return Result.success(report);
    }

    /**
     * 获取用户在指定时间范围内的支出情况。
     * @param startTime 起始时间（格式为 yyyy-MM-dd）
     * @param endTime 结束时间（格式为 yyyy-MM-dd）
     * @param userID 用户 ID
     * @return 时间汇总报告对象
     */
    @GetMapping("/report/time")
    public Result getTimeExpense(String startTime, String endTime, Integer userID) {
        TimeReport report = expenseService.getTimeExpense(startTime, endTime, userID);
        return Result.success(report);
    }

    /**
     * 获取某用户所有行程预算与支出汇总报告。
     * @param userID 用户 ID
     * @return 行程预算支出总览对象
     */
    @GetMapping("/report/budgetandexpense")
    public Result getAllBudgetAndExpense(Integer userID) {
        ItineraryReport report = reportService.getAllBudgetAndExpense(userID);
        return Result.success(report);
    }

    /**
     * 获取指定行程 ID 列表对应的预算与支出报告。
     * @param itiIDs 行程 ID 列表
     * @return 行程预算支出总览对象
     */
    @GetMapping("/report/iti")
    public Result getItiIDsBudgetAndExpense(@RequestParam List<Integer> itiIDs) {
        ItineraryReport report = reportService.getEventBudgetAndExpense(itiIDs);
        return Result.success(report);
    }
}
