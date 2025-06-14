package org.example.tmfinance.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 支出实体类，对应数据库中的 expense 表。
 * 记录用户在某个事件下的支出明细，包括类型、时间、金额和名称。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("expense")  // 映射到数据库中的 expense 表
public class Expense {

    /**
     * 支出记录主键，自增 ID。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应的事件 ID（外键）。
     */
    @TableField("eve_id")
    private Integer eveID;

    /**
     * 支出类型编号（与前端分类表对应）。
     */
    @TableField("type")
    private Integer type;

    /**
     * 支出时间，格式为 yyyy-MM-dd HH:mm:ss。
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 支出金额（单位：元）。
     */
    @TableField("money")
    private Float money;

    /**
     * 支出项名称（如餐饮、交通等）。
     */
    @TableField("name")
    private String name;
}
