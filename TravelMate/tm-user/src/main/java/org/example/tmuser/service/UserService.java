package org.example.tmuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmuser.pojo.User;

public interface UserService extends IService<User> {
    Integer login(String code);
    Integer updateUser(User user);
    User getUserByID(Integer id);
}
