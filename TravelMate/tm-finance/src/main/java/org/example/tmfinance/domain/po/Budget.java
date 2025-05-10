package org.example.tmfinance.domain.po;

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
@TableName("budget")  //MP中对应上表
public class Budget {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    @TableField("eve_id")
    Integer eveID;
    @TableField("money")
    Float money;
}
