package org.example.tmplan.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("itinerary")  //MP中对应上表
public class Itinerary {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;
    @TableField("user_id")
    private Integer userID;
    @TableField("name")
    private String name;
    @TableField("start_date")
    private LocalDate startDate;
    @TableField("end_date")
    private LocalDate endDate;
    @TableField("location")
    private String location;
}
