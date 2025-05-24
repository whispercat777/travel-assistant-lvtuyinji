package org.example.tmfinance.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 时间维度支出统计报告类。
 * 封装了某时间范围或全部时间内的支出记录及其总金额。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeReport {

    /**
     * 支出记录列表，通常按时间范围查询所得。
     */
    private List<Expense> expenses;

    /**
     * 支出总金额（单位：元），由支出记录中的金额字段累计而得。
     */
    private Integer totalExpense;
}
