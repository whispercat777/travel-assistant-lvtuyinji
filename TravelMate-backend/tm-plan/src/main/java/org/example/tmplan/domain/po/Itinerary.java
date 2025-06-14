package org.example.tmplan.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 行程实体类，对应数据库中的 itinerary 表。
 * 表示用户的一个完整出行计划，包括起止时间、地点等基础信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("itinerary")  // 映射至数据库 itinerary 表
public class Itinerary {

    /**
     * 主键 ID，自增。
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    /**
     * 所属用户 ID（外键），关联用户表。
     */
    @TableField("user_id")
    private Integer userID;

    /**
     * 行程名称（如“2025暑假西安之旅”）。
     */
    @TableField("name")
    private String name;

    /**
     * 行程开始日期。
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 行程结束日期。
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 行程地点（城市或区域）。
     */
    @TableField("location")
    private String location;
}
