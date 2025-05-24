package org.example.tmuser.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类。
 * 映射数据库中的 user 表，表示系统中的一位用户基本信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")  // 对应数据库中的 user 表
public class User {

    /**
     * 用户主键 ID，数据库自增。
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    /**
     * 微信用户唯一标识 openID。
     * 用于标识不同用户，来源于微信登录。
     */
    @TableField("openID")
    private String openID;

    /**
     * 用户姓名或昵称。
     */
    @TableField("name")
    private String name;

    /**
     * 用户性别：0-未知，1-男，2-女。
     */
    @TableField("gender")
    private Integer gender;
}
