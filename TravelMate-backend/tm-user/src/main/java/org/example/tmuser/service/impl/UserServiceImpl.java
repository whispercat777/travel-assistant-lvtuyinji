package org.example.tmuser.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.tmuser.mapper.UserMapper;
import org.example.tmuser.domain.po.User;
import org.example.tmuser.service.UserService;
import org.example.tmuser.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类。
 * 实现用户的登录、信息更新、查询等功能，封装对数据库的操作逻辑。
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录（微信小程序登录流程）。
     * 通过调用微信 API 获取 openID，检查数据库中是否已有该用户：
     * - 如果存在，返回该用户 ID；
     * - 如果不存在，自动注册并返回新用户 ID。
     *
     * @param code 微信临时登录凭证 code
     * @return 用户 ID
     */
    @Override
    public Integer login(String code) {
        String openID = null;

        // 1. 调用微信 API 获取 openID
        try {
            log.info("code={}", code);
            JSONObject result = WeChatUtil.getSessionKeyOrOpenId(code);
            openID = result.get("openid", String.class);
            if (openID != null) {
                System.out.println("OpenID: " + openID);
            } else {
                System.out.println("获取 openid 失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用微信接口失败");
        }

        // 2. 创建用户对象并设置 openID
        User user = new User();
        user.setOpenID(openID);

        // 3. 检查数据库中是否已有该 openID
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenID, openID);
        User existingUser = userMapper.selectOne(queryWrapper);

        // 4. 如果存在，返回已有用户 ID
        if (existingUser != null) {
            return existingUser.getID();
        }

        // 5. 如果不存在，插入用户记录，返回新用户 ID
        userMapper.insert(user);
        return user.getID();
    }

    /**
     * 根据 ID 更新用户信息（仅更新非空字段）。
     *
     * @param user 包含更新信息的用户对象
     * @return 用户 ID（更新成功）
     */
    @Override
    public Integer updateUser(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getID, user.getID())
                .set(user.getName() != null, User::getName, user.getName())
                .set(user.getGender() != null, User::getGender, user.getGender())
                .set(user.getOpenID() != null, User::getOpenID, user.getOpenID());

        userMapper.update(null, updateWrapper);
        return user.getID();
    }

    /**
     * 根据用户 ID 查询完整用户信息。
     *
     * @param id 用户主键 ID
     * @return 用户实体对象
     */
    @Override
    public User getUserByID(Integer id) {
        return userMapper.selectById(id);
    }
}
