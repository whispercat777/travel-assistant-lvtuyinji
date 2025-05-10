package org.example.tmplan.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("event")  //MP中对应上表
public class Event {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;
    @TableField("iti_id")
    private Integer itiID;
    @TableField("start_time")
    private LocalDateTime startTime;
    @TableField("end_time")
    private LocalDateTime endTime;
    @TableField("location")
    private String location;
    @TableField("description")
    private String description;
    @TableField("name")
    private String name;
    @TableField("type")
    private Integer type;
}
