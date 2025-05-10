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

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("reminder") // MP中对应数据库表
public class Reminder {
    @TableId(value = "id", type = IdType.AUTO) // 主键自动递增
    private Integer id;

    @TableField("time") // 对应字段 time
    private LocalDateTime time;

    @TableField("location") // 对应字段 location
    private String location;

    @TableField("userid") // 对应字段 userid
    private Integer userId;

    @TableField("date") // 对应字段 data
    private LocalDate date;
}
