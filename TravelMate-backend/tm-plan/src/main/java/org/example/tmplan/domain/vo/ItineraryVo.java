package org.example.tmplan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 行程视图对象（ItineraryVo）。
 * 用于前端展示或接口传输的行程数据，包含基础信息及其下属事件列表。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryVo {

    /**
     * 行程主键 ID。
     */
    private Integer ID;

    /**
     * 所属用户 ID。
     */
    private Integer userID;

    /**
     * 行程名称（如“暑假之旅”、“公司出差计划”）。
     */
    private String name;

    /**
     * 行程开始日期。
     */
    private LocalDate startDate;

    /**
     * 行程结束日期。
     */
    private LocalDate endDate;

    /**
     * 行程地点（如城市、国家、区域等）。
     */
    private String location;

    /**
     * 行程包含的事件列表，每个事件包含详细信息与预算/支出记录。
     */
    private List<EventVo> events;
}
