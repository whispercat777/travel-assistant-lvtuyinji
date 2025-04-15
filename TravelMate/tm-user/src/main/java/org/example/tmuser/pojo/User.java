package org.example.tmuser.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")  //MP中对应上表
public class User {
    @TableId(value = "ID", type = IdType.AUTO) // 主键自增
    private Integer ID;
    @TableField("openID")
    private String openID;
    @TableField("name")
    private String name;
    @TableField("gender")
    private Integer gender;
}
