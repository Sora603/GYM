package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.entity.UserBodyInfo;
import com.gym.service.UserBodyInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/body")
public class UserBodyController {

    @Resource
    private UserBodyInfoService userBodyInfoService;

    private User requireLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("未登录");
        }
        return user;
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    /**
     * 用户上传身体数据
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Map<String, Object> body, HttpSession session) {
        try {
            User user = requireLogin(session);
            BigDecimal height = new BigDecimal(body.get("height").toString());
            BigDecimal weight = new BigDecimal(body.get("weight").toString());
            Integer gender = Integer.parseInt(body.get("gender").toString());
            Integer age = Integer.parseInt(body.get("age").toString());

            UserBodyInfo info = userBodyInfoService.save(user.getId(), height, weight, gender, age);
            Map<String, Object> data = new HashMap<>();
            data.put("id", info.getId());
            data.put("height", info.getHeight());
            data.put("weight", info.getWeight());
            data.put("bmi", info.getBmi());
            data.put("bodyFat", info.getBodyFat());
            data.put("recordTime", info.getRecordTime());
            return Result.ok("保存成功", data);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户获取最新身体数据
     */
    @GetMapping("/latest")
    public Result<?> latest(HttpSession session) {
        try {
            User user = requireLogin(session);
            UserBodyInfo info = userBodyInfoService.getLatest(user.getId());
            if (info == null) {
                return Result.ok(null);
            }
            Map<String, Object> data = new HashMap<>();
            data.put("id", info.getId());
            data.put("height", info.getHeight());
            data.put("weight", info.getWeight());
            data.put("gender", info.getGender());
            data.put("age", info.getAge());
            data.put("bmi", info.getBmi());
            data.put("bodyFat", info.getBodyFat());
            data.put("recordTime", info.getRecordTime());
            return Result.ok(data);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户获取历史身体数据（曲线图用）
     */
    @GetMapping("/history")
    public Result<?> history(HttpSession session) {
        try {
            User user = requireLogin(session);
            List<UserBodyInfo> list = userBodyInfoService.getHistory(user.getId());
            return Result.ok(list);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户删除某条记录
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, HttpSession session) {
        try {
            User user = requireLogin(session);
            userBodyInfoService.deleteRecord(id, user.getId());
            return Result.ok("删除成功", null);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 管理员查看所有会员最新身体数据
     */
    @GetMapping("/admin/members")
    public Result<?> adminBodyList(HttpSession session) {
        if (!isAdmin(session)) {
            return Result.fail(403, "无权限");
        }
        List<Map<String, Object>> list = userBodyInfoService.getAllMembersLatest();
        return Result.ok(list);
    }

    /**
     * 管理员查看某会员历史身体数据
     */
    @GetMapping("/admin/member/{userId}")
    public Result<?> adminMemberHistory(@PathVariable Long userId, HttpSession session) {
        if (!isAdmin(session)) {
            return Result.fail(403, "无权限");
        }
        List<UserBodyInfo> list = userBodyInfoService.getHistory(userId);
        return Result.ok(list);
    }

    /**
     * 管理员删除某条身体数据记录
     */
    @DeleteMapping("/admin/{id}")
    public Result<?> adminDelete(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return Result.fail(403, "无权限");
        }
        userBodyInfoService.deleteById(id);
        return Result.ok("删除成功", null);
    }
}
