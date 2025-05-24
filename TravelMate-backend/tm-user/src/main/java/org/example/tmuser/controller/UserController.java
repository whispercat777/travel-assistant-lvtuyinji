package org.example.tmuser.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmuser.domain.po.Result;
import org.example.tmuser.domain.po.User;
import org.example.tmuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器。
 * 提供用户登录、信息完善、信息查询等接口，供前端调用。
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口。
     * 前端通过微信小程序获取临时登录 code，后端调用微信接口换取用户唯一标识。
     *
     * @param code 微信临时登录凭证
     * @return 成功返回用户 ID
     */
    @GetMapping("/user/login")
    public Result login(String code) {
        Integer id = userService.login(code);
        return Result.success(id);
    }

    /**
     * 用户信息完善接口。
     * 用于用户登录后提交个人信息（如昵称、头像等）进行补全。
     *
     * @param user 用户信息对象
     * @return 成功返回用户 ID
     */
    @PutMapping("/user/info")
    public Result completeInfo(@RequestBody User user) {
        Integer id = userService.updateUser(user);
        return Result.success(id);
    }

    /**
     * 获取用户详细信息接口。
     *
     * @param userID 用户 ID
     * @return 用户完整信息（User 对象）
     */
    @GetMapping("/user/info")
    public Result getUserInfo(Integer userID) {
        User user = userService.getUserByID(userID);
        return Result.success(user);
    }
}
