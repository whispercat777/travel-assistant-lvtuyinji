package org.example.tmfinance.service.impl;

import org.example.tmfinance.client.PlanClient;
import org.example.tmfinance.domain.po.Budget;
import org.example.tmfinance.domain.po.Expense;
import org.example.tmfinance.domain.po.Result;
import org.example.tmfinance.domain.po.ItineraryReport;
import org.example.tmfinance.service.BudgetService;
import org.example.tmfinance.service.ExpenseService;
import org.example.tmfinance.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private PlanClient planClient;
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetService budgetService;
    public ItineraryReport getAllBudgetAndExpense(Integer userID) {
        // 调用 PlanClient 获取 userID 对应的 eveID 列表
        Result result = planClient.getAllEvent(userID);
        // 提取返回的 eveID 列表
        List<Integer> eveIDs = (List<Integer>) result.getData();
        if (eveIDs == null || eveIDs.isEmpty()) {
            return new ItineraryReport(Collections.emptyList(), Collections.emptyList(), 0, 0); // 无相关事件
        }
        // 用于存储所有的费用和预算
        List<Expense> allExpenses = new ArrayList<>();
        List<Budget> allBudgets = new ArrayList<>();
        int totalExpense = 0;
        int totalBudget = 0;

        // 遍历每个 eveID
        for (Integer eveID : eveIDs) {
            // 获取当前事件的费用列表
            List<Expense> expenses = expenseService.getEventExpense(eveID);
            allExpenses.addAll(expenses);
            // 累计总费用
            totalExpense += expenses.stream()
                    .mapToInt(expense -> Math.round(expense.getMoney()))
                    .sum();

            // 获取当前事件的预算列表
            List<Budget> budgets = budgetService.getEventBudget(eveID);
            allBudgets.addAll(budgets);
            // 累计总预算
            totalBudget += budgets.stream()
                    .mapToInt(budget -> Math.round(budget.getMoney()))
                    .sum();
        }

        // 构造 Report2 对象
        return new ItineraryReport(allExpenses, allBudgets, totalExpense, totalBudget);
    }
    public ItineraryReport getEventBudgetAndExpense(List<Integer> itiIDs) {
        Result result = planClient.getItiIDsEvent(itiIDs);
        // 提取返回的 eveID 列表
        List<Integer> eveIDs = (List<Integer>) result.getData();
        if (eveIDs == null || eveIDs.isEmpty()) {
            return new ItineraryReport(Collections.emptyList(), Collections.emptyList(), 0, 0); // 无相关事件
        }

        // 用于存储所有的费用和预算
        List<Expense> allExpenses = new ArrayList<>();
        List<Budget> allBudgets = new ArrayList<>();
        int totalExpense = 0;
        int totalBudget = 0;

        // 遍历每个 eveID
        for (Integer eveID : eveIDs) {
            // 获取当前事件的费用列表
            List<Expense> expenses = expenseService.getEventExpense(eveID);
            allExpenses.addAll(expenses);
            // 累计总费用
            totalExpense += expenses.stream()
                    .mapToInt(expense -> Math.round(expense.getMoney()))
                    .sum();

            // 获取当前事件的预算列表
            List<Budget> budgets = budgetService.getEventBudget(eveID);
            allBudgets.addAll(budgets);
            // 累计总预算
            totalBudget += budgets.stream()
                    .mapToInt(budget -> Math.round(budget.getMoney()))
                    .sum();
        }

        // 构造 Report2 对象
        return new ItineraryReport(allExpenses, allBudgets, totalExpense, totalBudget);
    }

}
