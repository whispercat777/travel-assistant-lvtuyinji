package org.example.tmfinance.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类型维度支出统计报告类。
 * 用于封装某用户在多个支出类型下的支出明细与总金额。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeReport {

    /**
     * 支出记录列表，按类型条件筛选的结果。
     */
    private List<Expense> expenses;

    /**
     * 总支出金额（单位：元），为所有类型下支出金额之和。
     */
    private Integer totalExpense;
}
