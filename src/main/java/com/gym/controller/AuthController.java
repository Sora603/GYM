package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, String> body) {
        try {
            User user = userService.register(
                body.get("username"),
                body.get("password"),
                body.get("phone"),
                body.get("photo")
            );
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("phone", user.getPhone());
            return Result.ok("注册成功，请登录后购买健身卡获取VIP卡号", data);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            User user = userService.login(body.get("username"), body.get("password"));
            session.setAttribute("user", user);
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
            data.put("vipCardNo", user.getVipCardNo());
            data.put("phone", user.getPhone());
            return Result.ok("登录成功", data);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/me")
    public Result<?> me(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.fail(401, "未登录");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("vipCardNo", user.getVipCardNo());
        data.put("phone", user.getPhone());
        return Result.ok(data);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.invalidate();
        return Result.ok("已退出", null);
    }

    @PostMapping("/change-password")
    public Result<?> changePassword(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.fail(401, "未登录");
        }
        try {
            userService.changePassword(user.getId(), body.get("oldPassword"), body.get("newPassword"));
            return Result.ok("密码修改成功", null);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }
}
