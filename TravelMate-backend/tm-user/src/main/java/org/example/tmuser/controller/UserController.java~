package org.example.tmuser.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tmuser.domain.po.Result;
import org.example.tmuser.domain.po.User;
import org.example.tmuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/login")
    public Result login(String code) {;
        Integer id = userService.login(code);
        return Result.success(id);
    }
    @PutMapping("/user/info")
    public Result completeInfo(@RequestBody User user) {
        Integer id=userService.updateUser(user);
        return Result.success(id);
    }
    @GetMapping("/user/info")
    public Result getUserInfo(Integer userID) {
        User user = userService.getUserByID(userID);
        return Result.success(user);
    }

}
