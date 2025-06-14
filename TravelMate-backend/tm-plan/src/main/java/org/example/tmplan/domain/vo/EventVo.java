package org.example.tmplan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 事件视图对象（EventVo）。
 * 用于前端展示或表单交互的复合数据结构，包含事件基本信息及其相关的支出和预算列表。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventVo {

    /**
     * 事件主键 ID。
     */
    private Integer ID;

    /**
     * 所属行程 ID。
     */
    private Integer itiID;

    /**
     * 事件开始时间。
     */
    private LocalDateTime startTime;

    /**
     * 事件结束时间。
     */
    private LocalDateTime endTime;

    /**
     * 事件地点（如城市、场所等）。
     */
    private String location;

    /**
     * 事件详细描述。
     */
    private String description;

    /**
     * 事件名称。
     */
    private String name;

    /**
     * 事件类型编号（例如 1-交通，2-住宿等）。
     */
    private Integer type;

    /**
     * 本事件下的所有支出记录。
     */
    private List<Expense> expenses;

    /**
     * 本事件下的所有预算记录。
     */
    private List<Budget> budgets;
}
