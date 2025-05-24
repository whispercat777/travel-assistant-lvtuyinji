package org.example.tmfinance.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 行程预算与支出报告实体类。
 * 用于封装某用户或某行程下所有事件的预算和支出信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryReport {

    /**
     * 所有事件的支出记录集合。
     */
    private List<Expense> expenses;

    /**
     * 所有事件的预算记录集合。
     */
    private List<Budget> budgets;

    /**
     * 所有事件支出的总金额（单位：元）。
     */
    private Integer totalExpense;

    /**
     * 所有事件预算的总金额（单位：元）。
     */
    private Integer totalBudget;
}
