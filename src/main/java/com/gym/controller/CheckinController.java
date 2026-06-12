package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.MotivationalQuote;
import com.gym.entity.User;
import com.gym.mapper.MotivationalQuoteMapper;
import com.gym.service.CheckinService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    @Resource
    private CheckinService checkinService;

    @Resource
    private MotivationalQuoteMapper quoteMapper;

    /**
     * 用户打卡
     */
    @PostMapping
    public Result<?> doCheckin(@RequestBody(required = false) Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");

        String mood = body != null ? body.get("mood") : null;
        Map<String, Object> result = checkinService.doCheckin(user.getId(), mood);
        if (!(boolean) result.get("success")) {
            return Result.fail((String) result.get("message"));
        }
        return Result.ok((String) result.get("message"), result);
    }

    /**
     * 用户打卡状态
     */
    @GetMapping("/status")
    public Result<?> getStatus(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");

        Map<String, Object> status = checkinService.getStatus(user.getId());
        // 如果今日已打卡，返回一条名言
        if (Boolean.TRUE.equals(status.get("checkedToday"))) {
            MotivationalQuote quote = quoteMapper.randomOne();
            if (quote != null) {
                Map<String, String> q = new HashMap<>();
                q.put("content", quote.getContent());
                q.put("author", quote.getAuthor() != null ? quote.getAuthor() : "");
                status.put("todayQuote", q);
            }
        }
        return Result.ok(status);
    }

    /**
     * 某月打卡日期列表
     */
    @GetMapping("/month")
    public Result<?> getMonth(@RequestParam int year, @RequestParam int month, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        return Result.ok(checkinService.getMonthCheckins(user.getId(), year, month));
    }

    /**
     * 某年打卡日期列表（热力图用）
     */
    @GetMapping("/year")
    public Result<?> getYear(@RequestParam int year, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(403, "请先登录");
        return Result.ok(checkinService.getYearCheckins(user.getId(), year));
    }
}
