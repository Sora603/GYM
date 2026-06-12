package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Coach;
import com.gym.entity.User;
import com.gym.service.CoachService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/coach")
public class CoachController {

    @Resource
    private CoachService coachService;

    // 公开 - 查看在职教练
    @GetMapping("/list")
    public Result<?> list() {
        return Result.ok(coachService.getActiveCoaches());
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(coachService.getById(id));
    }

    // 管理员操作
    @GetMapping("/admin/all")
    public Result<?> adminList(HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(coachService.getAllCoaches());
    }

    @PostMapping("/admin/add")
    public Result<?> add(@RequestBody Coach coach, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(coachService.addCoach(coach));
    }

    @PutMapping("/admin/update")
    public Result<?> update(@RequestBody Coach coach, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(coachService.updateCoach(coach));
    }

    @DeleteMapping("/admin/delete/{id}")
    public Result<?> delete(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        coachService.deleteCoach(id);
        return Result.ok("删除成功", null);
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }
}
