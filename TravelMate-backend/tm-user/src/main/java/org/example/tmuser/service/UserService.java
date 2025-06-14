package org.example.tmuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmuser.domain.po.User;

/**
 * 用户服务接口。
 * 提供用户登录、信息更新与查询等功能的定义。
 * 继承 MyBatis-Plus 的 IService 接口，具备基础 CRUD 能力。
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录（微信小程序登录流程）。
     * 根据 code 调用微信接口获取 openID，判断用户是否存在，自动注册或返回用户 ID。
     *
     * @param code 微信登录凭证 code
     * @return 用户主键 ID
     */
    Integer login(String code);

    /**
     * 更新用户信息（仅更新非空字段）。
     *
     * @param user 包含更新内容的用户对象
     * @return 更新后的用户 ID
     */
    Integer updateUser(User user);

    /**
     * 根据用户 ID 查询用户信息。
     *
     * @param id 用户主键 ID
     * @return 对应的用户对象
     */
    User getUserByID(Integer id);
}
