package org.example.tmplan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 支出视图对象（Expense VO）。
 * 用于前端展示、表单编辑或接口数据交互，表示单条支出记录。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    /**
     * 支出记录主键 ID。
     */
    private Integer id;

    /**
     * 所属事件 ID。
     */
    private Integer eveID;

    /**
     * 支出类型编号（如 1-交通，2-餐饮 等）。
     */
    private Integer type;

    /**
     * 支出时间（格式：yyyy-MM-dd HH:mm:ss）。
     */
    private LocalDateTime time;

    /**
     * 支出金额（单位：元）。
     */
    private Float money;

    /**
     * 支出项名称（如“打车”、“午餐”）。
     */
    private String name;
}
