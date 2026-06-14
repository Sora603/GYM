package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.service.CustomPlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/training/custom")
public class CustomPlanController {

    @Resource
    private CustomPlanService customPlanService;

    /**
     * 获取自定义训练计划
     */
    @GetMapping("/plan")
    public Result<?> getPlan(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        return Result.ok(customPlanService.getCustomTraining(user.getId()));
    }

    /**
     * 保存某天训练计划
     */
    @PutMapping("/plan")
    public Result<?> savePlan(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        customPlanService.saveCustomTraining(user.getId(), body);
        return Result.ok("已保存", null);
    }

    /**
     * 获取自定义饮食计划
     */
    @GetMapping("/diet")
    public Result<?> getDiet(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        return Result.ok(customPlanService.getCustomDiet(user.getId()));
    }

    /**
     * 保存某天饮食计划
     */
    @PutMapping("/diet")
    public Result<?> saveDiet(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        customPlanService.saveCustomDiet(user.getId(), body);
        return Result.ok("已保存", null);
    }

    /**
     * 从系统模板重置
     */
    @PostMapping("/reset")
    public Result<?> resetFromTemplate(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        String goal = body.getOrDefault("goal", "BUILD");
        customPlanService.resetFromTemplate(user.getId(), goal);
        return Result.ok("已重置", null);
    }

    /**
     * 一键清空
     */
    @PostMapping("/clear")
    public Result<?> clearAll(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        customPlanService.clearAll(user.getId());
        return Result.ok("已清空", null);
    }

    /**
     * 获取食物库
     */
    @GetMapping("/foods")
    public Result<?> getFoods() {
        return Result.ok(customPlanService.getFoodLibrary());
    }

    /**
     * 获取动作库
     */
    @GetMapping("/exercises")
    public Result<?> getExercises() {
        return Result.ok(customPlanService.getExerciseLibrary());
    }
}
