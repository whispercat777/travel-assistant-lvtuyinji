package org.example.tmuser.service.impl;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.tmuser.mapper.UserMapper;
import org.example.tmuser.pojo.User;
import org.example.tmuser.service.UserService;
import org.example.tmuser.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Integer login(String code) {
        String openID = null;
        // 1. 调用微信 API 获取 openID
        try {
            log.info("code={}", code);
            JSONObject result = WeChatUtil.getSessionKeyOrOpenId(code);
            openID = result.get("openid", String.class);  // 获取到的 openid
            if (openID != null) {
                System.out.println("OpenID: " + openID);
                // 进行后续的操作，例如登录、注册等
            } else {
                System.out.println("获取 openid 失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用微信接口失败");
        }
        // 2. 设置 openID 到 User 对象中
        User user = new User();
        user.setOpenID(openID);

        // 3. 查询数据库中是否存在该 openID
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenID, openID);
        User existingUser = userMapper.selectOne(queryWrapper);

        // 4. 如果存在，直接返回 ID
        if (existingUser != null) {
            return existingUser.getID();
        }

        // 5. 如果不存在，插入用户信息并返回 ID
        userMapper.insert(user); // 插入后 MyBatis-Plus 会自动填充 ID
        return user.getID();
    }

    // 根据 ID 更新用户信息
    @Override
    public Integer updateUser(User user) {
        // 创建 LambdaUpdateWrapper 来构建动态的更新条件
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getID, user.getID()) // 根据 ID 进行更新
                .set(user.getName() != null, User::getName, user.getName())  // 如果 name 不为空，更新 name
                .set(user.getGender() != null, User::getGender, user.getGender())  // 如果 gender 不为空，更新 gender
                .set(user.getOpenID() != null, User::getOpenID, user.getOpenID());  // 如果 openID 不为空，更新 openID

        // 执行更新操作
        userMapper.update(null, updateWrapper);
        return user.getID();
    }
    @Override
    public User getUserByID(Integer id) {
        return userMapper.selectById(id); // 使用 MyBatis-Plus 提供的 selectById 方法
    }


}
