package org.example.tmuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tmuser.domain.po.User;

/**
 * 用户表（user）对应的数据访问层接口。
 * 继承 MyBatis-Plus 提供的 BaseMapper，具备通用 CRUD 功能。
 * 无需手写 SQL，即可通过方法名进行基础数据库操作。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 如有自定义查询，可在此扩展定义对应方法
}
