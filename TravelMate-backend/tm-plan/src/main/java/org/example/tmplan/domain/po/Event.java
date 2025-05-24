package org.example.tmplan.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 事件实体类，对应数据库中的 event 表。
 * 用于表示行程下的具体活动安排，如某一时间段的旅游、会议等。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("event")  // 对应数据库中的 event 表
public class Event {

    /**
     * 主键 ID，自增。
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    /**
     * 所属行程 ID（外键），关联 itinerary 表。
     */
    @TableField("iti_id")
    private Integer itiID;

    /**
     * 事件开始时间。
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 事件结束时间。
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 事件地点（城市、景点、会议室等）。
     */
    @TableField("location")
    private String location;

    /**
     * 事件描述信息（如“参观博物馆”、“开会”）。
     */
    @TableField("description")
    private String description;

    /**
     * 事件名称（如“第一天行程”、“客户拜访”）。
     */
    @TableField("name")
    private String name;

    /**
     * 事件类型编号，用于分类（如1代表出行，2代表用餐等）。
     */
    @TableField("type")
    private Integer type;
}
