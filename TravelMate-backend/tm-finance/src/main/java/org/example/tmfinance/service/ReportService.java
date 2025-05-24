package org.example.tmfinance.service;

import org.example.tmfinance.domain.po.ItineraryReport;

import java.util.List;

/**
 * 报告服务接口。
 * 提供行程维度的预算与支出统计功能，支持按用户或指定行程列表进行汇总分析。
 */
public interface ReportService {

    /**
     * 获取指定用户下所有事件的预算与支出汇总报告。
     *
     * @param userID 用户 ID
     * @return ItineraryReport 对象，包含所有支出、预算记录及其总额
     */
    ItineraryReport getAllBudgetAndExpense(Integer userID);

    /**
     * 获取指定行程 ID 列表下所有事件的预算与支出汇总报告。
     *
     * @param itiIDs 行程 ID 列表
     * @return ItineraryReport 对象，包含所有支出、预算记录及其总额
     */
    ItineraryReport getEventBudgetAndExpense(List<Integer> itiIDs);
}
