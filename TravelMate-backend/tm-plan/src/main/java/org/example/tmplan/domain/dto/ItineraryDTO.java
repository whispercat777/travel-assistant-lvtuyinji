package org.example.tmplan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 行程信息数据传输对象（ItineraryDTO）。
 * 用于在接口层接收或传递用户创建或查询行程所需的基本信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDTO {

    /**
     * 行程开始日期（格式：yyyy-MM-dd）。
     */
    private LocalDate startDate;

    /**
     * 行程结束日期（格式：yyyy-MM-dd）。
     */
    private LocalDate endDate;

    /**
     * 行程地点或目的地名称。
     */
    private String location;
}
