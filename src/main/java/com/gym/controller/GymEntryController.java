package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.service.GymDailyService;
import org.springframework.web.bind.annotation.*;

import com.gym.entity.GymDaily;
import com.gym.entity.GymTimeslot;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gym")
public class GymEntryController {

    @Resource
    private GymDailyService gymDailyService;

    // 公开 - 获取当前在场人数
    @GetMapping("/count")
    public Result<?> getCount() {
        return Result.ok(gymDailyService.getTodayRecord());
    }

    // 手机号入场（管理员操作）
    @PostMapping("/enter")
    public Result<?> enter(@RequestBody Map<String, String> body, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
            return Result.fail(403, "无权限");
        }
        Map<String, Object> result = gymDailyService.enterByPhone(body.get("phone"));
        boolean success = (boolean) result.get("success");
        if (success) {
            Map<String, Object> data = new HashMap<>();
            data.put("user", result.get("user"));
            data.put("cards", result.get("cards"));
            data.put("gymDaily", gymDailyService.getTodayRecord());
            return Result.ok((String) result.get("message"), data);
        } else {
            return Result.fail((String) result.get("message"));
        }
    }

    // 手机号查会员（不入场）
    @PostMapping("/lookup")
    public Result<?> lookup(@RequestBody Map<String, String> body, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
            return Result.fail(403, "无权限");
        }
        Map<String, Object> result = gymDailyService.lookupByPhone(body.get("phone"));
        boolean success = (boolean) result.get("success");
        if (success) {
            Map<String, Object> data = new HashMap<>();
            data.put("user", result.get("user"));
            data.put("cards", result.get("cards"));
            return Result.ok("查询成功", data);
        } else {
            return Result.fail((String) result.get("message"));
        }
    }

    // 手动减人数（管理员操作）
    @PostMapping("/decrement")
    public Result<?> decrement(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return Result.fail(403, "无权限");
        }
        gymDailyService.decrementCount();
        return Result.ok("人数已减", gymDailyService.getTodayRecord());
    }

    // 指定人员退场
    @PostMapping("/exit/{entryLogId}")
    public Result<?> exitEntry(@PathVariable Long entryLogId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return Result.fail(403, "无权限");
        }
        String err = gymDailyService.exitEntry(entryLogId);
        if (err != null) {
            return Result.fail(err);
        }
        return Result.ok("已退场", gymDailyService.getTodayRecord());
    }

    // 获取当前在场人员
    @GetMapping("/active-entries")
    public Result<?> activeEntries(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return Result.fail(403, "无权限");
        }
        return Result.ok(gymDailyService.getActiveEntries());
    }

    // 公开 - 当月每日峰值
    @GetMapping("/daily-peaks")
    public Result<?> dailyPeaks(@RequestParam(defaultValue = "0") int year,
                                @RequestParam(defaultValue = "0") int month) {
        java.time.LocalDate now = java.time.LocalDate.now();
        if (year == 0) year = now.getYear();
        if (month == 0) month = now.getMonthValue();
        List<GymDaily> list = gymDailyService.getMonthPeaks(year, month);
        Map<String, Object> data = new HashMap<>();
        data.put("year", year);
        data.put("month", month);
        data.put("records", list);
        return Result.ok(data);
    }

    // 公开 - 今日(或指定日期)各时间段峰值
    @GetMapping("/timeslots")
    public Result<?> timeslots(@RequestParam(required = false) String date) {
        java.time.LocalDate queryDate;
        if (date != null && !date.isEmpty()) {
            queryDate = java.time.LocalDate.parse(date);
        } else {
            queryDate = java.time.LocalDate.now();
        }
        List<GymTimeslot> list = gymDailyService.getTimeslotsByDate(queryDate);
        String[] allSlots = {"8:00-10:00","10:00-12:00","12:00-14:00","14:00-16:00","16:00-20:00","20:00-22:00"};
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        for (String s : allSlots) result.put(s, 0);
        for (GymTimeslot t : list) result.put(t.getTimeSlot(), t.getPeakCount());
        Map<String, Object> data = new HashMap<>();
        data.put("date", queryDate.toString());
        data.put("slots", result);
        return Result.ok(data);
    }
}
