package org.example.tmuser.controller;

import org.example.tmuser.domain.po.Result;
import org.example.tmuser.domain.po.User;
import org.example.tmuser.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    void testLogin() {
        // Arrange
        String code = "test-code";
        when(userService.login(code)).thenReturn(123);

        // Act
        Result result = userController.login(code);

        // Assert
        assertEquals(123, result.getData());
        verify(userService, times(1)).login(code);
    }

    @Test
    void testCompleteInfo() {
        // Arrange
        User user = new User();
        user.setID(456);
        user.setName("TestUser");
        when(userService.updateUser(any(User.class))).thenReturn(456);

        // Act
        Result result = userController.completeInfo(user);

        // Assert
        assertEquals(456, result.getData());
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    void testGetUserInfo() {
        // Arrange
        Integer userId = 789;
        User user = new User();
        user.setID(userId);
        user.setName("InfoUser");

        when(userService.getUserByID(userId)).thenReturn(user);

        // Act
        Result result = userController.getUserInfo(userId);

        // Assert
        assertEquals(user, result.getData());
        verify(userService, times(1)).getUserByID(userId);
    }
}
