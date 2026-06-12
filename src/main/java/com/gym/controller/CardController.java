package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.entity.UserCard;
import com.gym.service.CardService;
import com.gym.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/card")
public class CardController {

    @Resource
    private CardService cardService;

    @Resource
    private UserService userService;

    // 查看所有卡类型
    @GetMapping("/types")
    public Result<?> cardTypes() {
        return Result.ok(cardService.getAllCards());
    }

    // 购买卡
    @PostMapping("/buy/{cardId}")
    public Result<?> buy(@PathVariable Long cardId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(401, "请先登录");
        try {
            UserCard userCard = cardService.buyCard(user.getId(), cardId);
            // 刷新session中的用户信息（VIP号可能在购卡时生成）
            User freshUser = userService.getById(user.getId());
            session.setAttribute("user", freshUser);
            // 返回信息中带上VIP号
            Map<String, Object> result = new HashMap<>();
            result.put("card", userCard);
            result.put("vipCardNo", freshUser.getVipCardNo());
            return Result.ok("购买成功", result);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    // 查看我的卡
    @GetMapping("/my")
    public Result<?> myCards(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(401, "请先登录");
        return Result.ok(cardService.getUserCards(user.getId()));
    }

    // 查看我的有效卡
    @GetMapping("/my/valid")
    public Result<?> myValidCards(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.fail(401, "请先登录");
        return Result.ok(cardService.getValidUserCards(user.getId()));
    }
}
