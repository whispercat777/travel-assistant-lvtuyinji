package org.example.tmuser.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmuser.pojo.Result;
import org.example.tmuser.pojo.User;
import org.example.tmuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/login")
    public Result login(String code) {
        log.info("login");
        Integer id = userService.login(code);
        log.info("id={}", id);
        return Result.success(id);
    }
    @PutMapping("/user/info")
    public Result completeInfo(@RequestBody User user) {
        log.info("completeUserInfo");
        Integer id=userService.updateUser(user);
        log.info("id={}", id);
        return Result.success(id);
    }
    @GetMapping("/user/info")
    public Result getUserInfo(Integer userID) {
        log.info("getUserInfo");
        User user = userService.getUserByID(userID);
        log.info("user={}", user);
        return Result.success(user);
    }

}
