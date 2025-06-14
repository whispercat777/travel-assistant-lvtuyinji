package org.example.tmuser.service.impl;

import cn.hutool.json.JSONObject;

import org.example.tmuser.domain.po.User;
import org.example.tmuser.mapper.UserMapper;
import org.example.tmuser.util.WeChatUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    void testLogin_NewUser() {
        String code = "test-code";
        String fakeOpenID = "openid-123";

        // 模拟静态方法 WeChatUtil.getSessionKeyOrOpenId()
        try (MockedStatic<WeChatUtil> mocked = Mockito.mockStatic(WeChatUtil.class)) {
            JSONObject mockResponse = new JSONObject();
            mockResponse.set("openid", fakeOpenID);
            mocked.when(() -> WeChatUtil.getSessionKeyOrOpenId(code)).thenReturn(mockResponse);

            // 模拟数据库不存在该 openID 的用户
            when(userMapper.selectOne(any())).thenReturn(null);

            // 模拟插入用户
            doAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setID(999); // 模拟 MyBatis-Plus 填充 ID
                return null;
            }).when(userMapper).insert(any(User.class));

            Integer result = userService.login(code);

            assertEquals(999, result);
        }
    }

    @Test
    void testLogin_ExistingUser() {
        String code = "test-code";
        String fakeOpenID = "openid-456";
        User existingUser = new User(123, fakeOpenID, "张三", 1);

        // 模拟微信接口
        try (MockedStatic<WeChatUtil> mocked = Mockito.mockStatic(WeChatUtil.class)) {
            JSONObject mockResponse = new JSONObject();
            mockResponse.set("openid", fakeOpenID);
            mocked.when(() -> WeChatUtil.getSessionKeyOrOpenId(code)).thenReturn(mockResponse);

            // 模拟数据库存在该用户
            when(userMapper.selectOne(any())).thenReturn(existingUser);

            Integer result = userService.login(code);

            assertEquals(123, result);
        }
    }

    @Test
    void testUpdateUser() {
        User user = new User(123, "openid-xyz", "新名字", 1);

        when(userMapper.update(any(), any())).thenReturn(1);

        Integer result = userService.updateUser(user);

        assertEquals(123, result);
        verify(userMapper, times(1)).update(any(), any());

    }


    @Test
    void testGetUserByID() {
        User user = new User(456, "openid-get", "测试用户", 0);
        when(userMapper.selectById(456)).thenReturn(user);

        User result = userService.getUserByID(456);

        assertEquals("测试用户", result.getName());
        assertEquals("openid-get", result.getOpenID());
    }
}
