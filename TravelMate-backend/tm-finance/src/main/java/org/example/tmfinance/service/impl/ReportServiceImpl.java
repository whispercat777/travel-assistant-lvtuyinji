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

/**
 * 报告服务实现类，实现行程级别的预算与支出分析功能。
 * 提供按用户 ID 或行程 ID 查询所有事件的预算与支出报告。
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private PlanClient planClient;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetService budgetService;

    /**
     * 获取某个用户下所有事件的预算与支出信息，并进行汇总。
     *
     * @param userID 用户 ID
     * @return ItineraryReport 对象，包括所有支出记录、预算记录、总支出金额、总预算金额
     */
    @Override
    public ItineraryReport getAllBudgetAndExpense(Integer userID) {
        Result result = planClient.getAllEvent(userID);
        List<Integer> eveIDs = (List<Integer>) result.getData();

        if (eveIDs == null || eveIDs.isEmpty()) {
            return new ItineraryReport(Collections.emptyList(), Collections.emptyList(), 0, 0);
        }

        List<Expense> allExpenses = new ArrayList<>();
        List<Budget> allBudgets = new ArrayList<>();
        int totalExpense = 0;
        int totalBudget = 0;

        // 遍历所有事件 ID，聚合支出与预算信息
        for (Integer eveID : eveIDs) {
            List<Expense> expenses = expenseService.getEventExpense(eveID);
            allExpenses.addAll(expenses);
            totalExpense += expenses.stream().mapToInt(e -> Math.round(e.getMoney())).sum();

            List<Budget> budgets = budgetService.getEventBudget(eveID);
            allBudgets.addAll(budgets);
            totalBudget += budgets.stream().mapToInt(b -> Math.round(b.getMoney())).sum();
        }

        return new ItineraryReport(allExpenses, allBudgets, totalExpense, totalBudget);
    }

    /**
     * 获取某些行程下所有事件的预算与支出信息，并进行汇总。
     *
     * @param itiIDs 行程 ID 列表
     * @return ItineraryReport 对象，包括所有支出记录、预算记录、总支出金额、总预算金额
     */
    @Override
    public ItineraryReport getEventBudgetAndExpense(List<Integer> itiIDs) {
        Result result = planClient.getItiIDsEvent(itiIDs);
        List<Integer> eveIDs = (List<Integer>) result.getData();

        if (eveIDs == null || eveIDs.isEmpty()) {
            return new ItineraryReport(Collections.emptyList(), Collections.emptyList(), 0, 0);
        }

        List<Expense> allExpenses = new ArrayList<>();
        List<Budget> allBudgets = new ArrayList<>();
        int totalExpense = 0;
        int totalBudget = 0;

        // 遍历所有事件 ID，聚合支出与预算信息
        for (Integer eveID : eveIDs) {
            List<Expense> expenses = expenseService.getEventExpense(eveID);
            allExpenses.addAll(expenses);
            totalExpense += expenses.stream().mapToInt(e -> Math.round(e.getMoney())).sum();

            List<Budget> budgets = budgetService.getEventBudget(eveID);
            allBudgets.addAll(budgets);
            totalBudget += budgets.stream().mapToInt(b -> Math.round(b.getMoney())).sum();
        }

        return new ItineraryReport(allExpenses, allBudgets, totalExpense, totalBudget);
    }
}
