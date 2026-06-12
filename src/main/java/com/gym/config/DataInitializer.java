package com.gym.config;

import com.gym.entity.User;
import com.gym.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DataInitializer implements CommandLineRunner {

    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        // 如果管理员不存在则创建
        User admin = userMapper.findByUsername("admin");
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole("ADMIN");
            userMapper.insert(admin);
            System.out.println(">>> 管理员账号已创建: admin / admin123");
        } else {
            // 确保密码是正确的hash
            String correctHash = encoder.encode("admin123");
            if (!encoder.matches("admin123", admin.getPassword())) {
                admin.setPassword(correctHash);
                userMapper.updateById(admin);
                System.out.println(">>> 管理员密码已重置: admin / admin123");
            }
        }

        // 如果测试会员不存在则创建，否则确保密码正确
        User test = userMapper.findByUsername("test");
        if (test == null) {
            test = new User();
            test.setUsername("test");
            test.setPassword(encoder.encode("test123"));
            test.setPhone("13800138000");
            // VIP卡号不再预分配，购卡时根据手机号自动生成
            test.setRole("USER");
            userMapper.insert(test);
            System.out.println(">>> 测试会员已创建: test / test123");
        } else {
            if (!encoder.matches("test123", test.getPassword())) {
                test.setPassword(encoder.encode("test123"));
                userMapper.updateById(test);
                System.out.println(">>> 测试会员密码已重置: test / test123");
            }
        }
    }
}
