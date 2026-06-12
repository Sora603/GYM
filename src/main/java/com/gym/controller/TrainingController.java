package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.mapper.UserMapper;
import com.gym.service.TrainingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Resource
    private TrainingService trainingService;

    @Resource
    private UserMapper userMapper;

    /**
     * 获取完整训练计划（含饮食）
     */
    @GetMapping("/plan")
    public Result<?> getPlan(@RequestParam(defaultValue = "") String goal, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");

        String goalType = goal;
        if (goalType == null || goalType.isEmpty()) {
            goalType = user.getFitnessGoal();
        }
        if (goalType == null || goalType.isEmpty()) {
            return Result.fail("请先选择训练目标");
        }
        if (!"BUILD".equals(goalType) && !"LOSE".equals(goalType)) {
            return Result.fail("无效的训练目标，请选择 BUILD(增肌) 或 LOSE(减脂)");
        }
        return Result.ok(trainingService.getFullPlan(goalType));
    }

    /**
     * 保存用户训练目标
     */
    @PostMapping("/goal")
    public Result<?> saveGoal(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");

        String goal = body.get("goal");
        if (goal == null || (!"BUILD".equals(goal) && !"LOSE".equals(goal))) {
            return Result.fail("无效的训练目标，请选择 BUILD(增肌) 或 LOSE(减脂)");
        }

        user.setFitnessGoal(goal);
        userMapper.updateById(user);
        session.setAttribute("user", user);
        return Result.ok("目标已保存", null);
    }

    /**
     * 获取用户当前训练目标
     */
    @GetMapping("/goal")
    public Result<?> getGoal(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        return Result.ok(user.getFitnessGoal());
    }
}
