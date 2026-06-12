package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gym.entity.User;
import com.gym.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 注册
    public User register(String username, String password, String phone, String photo) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("手机号不能为空");
        }
        User exist = userMapper.findByUsername(username);
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setPhone(phone);
        user.setPhoto(photo);
        user.setRole("USER");
        userMapper.insert(user);
        return user;
    }

    // 登录
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    // 管理员登录
    public User adminLogin(String username, String password) {
        User user = login(username, password);
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("非管理员账号");
        }
        return user;
    }

    // 根据ID获取用户
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    // 根据VIP卡号查询
    public User getByVipCardNo(String vipCardNo) {
        return userMapper.findByVipCardNo(vipCardNo);
    }

    // 根据手机号查询
    public User getByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }

    // 获取所有会员
    public List<User> getAllUsers() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRole, "USER");
        return userMapper.selectList(wrapper);
    }

    // 修改密码
    public void changePassword(Long userId, String oldPwd, String newPwd) {
        User user = userMapper.selectById(userId);
        if (!encoder.matches(oldPwd, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(encoder.encode(newPwd));
        userMapper.updateById(user);
    }

    // 管理员重置用户密码
    public void resetPassword(Long userId, String newPwd) {
        User user = userMapper.selectById(userId);
        user.setPassword(encoder.encode(newPwd));
        userMapper.updateById(user);
    }
}
