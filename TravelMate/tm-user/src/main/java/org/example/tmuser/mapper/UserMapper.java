package org.example.tmuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.tmuser.domain.po.User;
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
