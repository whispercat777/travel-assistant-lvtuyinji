package org.example.tmweather.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 天气提醒实体类。
 * 映射数据库中的 reminder 表，用于记录用户设置的天气提醒信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("reminder") // 指定数据库表名为 "reminder"
public class Reminder {

    /**
     * 提醒主键 ID，数据库自动递增。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 提醒的触发时间（精确到时分秒）。
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 提醒对应的地点（如城市名、地区名等）。
     */
    @TableField("location")
    private String location;

    /**
     * 用户 ID，表示该提醒属于哪个用户。
     */
    @TableField("userid")
    private Integer userId;

    /**
     * 天气提醒关联的日期（仅年月日，用于匹配天气数据）。
     */
    @TableField("date")
    private LocalDate date;
}
