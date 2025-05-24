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
 * 预算实体类，对应数据库中的 budget 表。
 * 包含事件预算的主键 ID、事件 ID 以及预算金额。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("budget")  // 映射到数据库中的 budget 表
public class Budget {

    /**
     * 预算记录主键，自增 ID。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应的事件 ID（外键）。
     */
    @TableField("eve_id")
    private Integer eveID;

    /**
     * 本次预算金额（单位：元）。
     */
    @TableField("money")
    private Float money;
}
