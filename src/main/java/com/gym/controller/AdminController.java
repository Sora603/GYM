package com.gym.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.Result;
import com.gym.entity.MembershipCard;
import com.gym.entity.SalesRecord;
import com.gym.entity.User;
import com.gym.entity.UserCard;
import com.gym.mapper.MembershipCardMapper;
import com.gym.mapper.SalesRecordMapper;
import com.gym.mapper.UserCardMapper;
import com.gym.mapper.UserMapper;
import com.gym.service.GymDailyService;
import com.gym.service.CardService;
import com.gym.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private UserCardMapper userCardMapper;

    @Resource
    private MembershipCardMapper membershipCardMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private GymDailyService gymDailyService;

    @Resource
    private UserService userService;

    @Resource
    private CardService cardService;

    @Resource
    private SalesRecordMapper salesRecordMapper;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getRole());
    }

    // 月度营收报表
    @GetMapping("/report/monthly")
    public Result<?> monthlyReport(HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");

        List<UserCard> allCards = userCardMapper.selectList(null);
        Map<String, BigDecimal> monthlyRevenue = new LinkedHashMap<>();

        for (UserCard uc : allCards) {
            String month = uc.getBuyTime().toLocalDate().toString().substring(0, 7); // YYYY-MM
            MembershipCard card = membershipCardMapper.selectById(uc.getCardId());
            if (card != null) {
                monthlyRevenue.merge(month, card.getPrice(), BigDecimal::add);
            }
        }

        // 补齐最近6个月
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        YearMonth now = YearMonth.now();
        for (int i = 5; i >= 0; i--) {
            String key = now.minusMonths(i).toString(); // YYYY-MM
            result.put(key, monthlyRevenue.getOrDefault(key, BigDecimal.ZERO));
        }

        return Result.ok(result);
    }

    // 会员列表（仅显示普通会员）
    @GetMapping("/members")
    public Result<?> members(HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        return Result.ok(userService.getAllUsers());
    }

    // 添加会员
    @PostMapping("/members")
    public Result<?> addMember(@RequestBody Map<String, String> body, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        try {
            User user = userService.register(
                body.get("username"),
                body.get("password"),
                body.get("phone"),
                body.get("photo")
            );
            return Result.ok("添加成功", user);
        } catch (RuntimeException e) {
            return Result.fail(e.getMessage());
        }
    }

    // 删除会员
    @DeleteMapping("/members/{id}")
    public Result<?> deleteMember(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        User user = userMapper.selectById(id);
        if (user == null) return Result.fail("会员不存在");
        // 删除购卡记录
        userCardMapper.delete(new LambdaQueryWrapper<UserCard>().eq(UserCard::getUserId, id));
        userMapper.deleteById(id);
        return Result.ok("已删除", null);
    }

    // 会员详情（含购卡记录）
    @GetMapping("/members/{id}")
    public Result<?> memberDetail(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        User user = userService.getById(id);
        if (user == null) return Result.fail("会员不存在");
        List<UserCard> cards = userCardMapper.findByUserId(id);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("cards", cards);
        return Result.ok(data);
    }

    // 编辑会员信息
    @PutMapping("/members/{id}")
    public Result<?> updateMember(@PathVariable Long id, @RequestBody Map<String, String> body, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        User user = userMapper.selectById(id);
        if (user == null) return Result.fail("会员不存在");
        if (body.containsKey("phone")) user.setPhone(body.get("phone"));
        if (body.containsKey("username")) user.setUsername(body.get("username"));
        if (body.containsKey("photo")) user.setPhoto(body.get("photo"));
        userMapper.updateById(user);
        return Result.ok("更新成功", null);
    }

    // 延长会员卡到期日
    @PostMapping("/members/{userId}/card/{cardId}/extend")
    public Result<?> extendCard(@PathVariable Long userId, @PathVariable Long cardId,
                                @RequestBody Map<String, Integer> body, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        UserCard userCard = userCardMapper.selectById(cardId);
        if (userCard == null || !userCard.getUserId().equals(userId)) {
            return Result.fail("购卡记录不存在");
        }
        int days = body.getOrDefault("days", 0);
        if (days <= 0) return Result.fail("天数必须大于0");
        userCard.setExpireTime(userCard.getExpireTime().plusDays(days));
        userCard.setStatus(1);
        userCardMapper.updateById(userCard);
        return Result.ok("已延长" + days + "天", null);
    }

    // 修复会员购卡记录叠加
    @PostMapping("/members/{userId}/fix-card-stacking")
    public Result<?> fixCardStacking(@PathVariable Long userId, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        cardService.fixCardStacking(userId);
        return Result.ok("购卡记录已修复", null);
    }

    // 仪表盘概览
    @GetMapping("/dashboard")
    public Result<?> dashboard(HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");

        Map<String, Object> data = new HashMap<>();

        // 总会员数
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getRole, "USER");
        data.put("totalMembers", userMapper.selectCount(userWrapper));

        // 本月收入（卡+商品）
        LocalDateTime startOfMonth = YearMonth.now().atDay(1).atStartOfDay();
        BigDecimal monthCardRevenue = BigDecimal.ZERO;
        List<UserCard> allCards = userCardMapper.selectList(null);
        for (UserCard uc : allCards) {
            if (!uc.getBuyTime().isBefore(startOfMonth)) {
                MembershipCard card = membershipCardMapper.selectById(uc.getCardId());
                if (card != null) monthCardRevenue = monthCardRevenue.add(card.getPrice());
            }
        }
        BigDecimal monthProductRevenue = salesRecordMapper.sumRevenueByDateRange(
            startOfMonth.toLocalDate(), LocalDate.now());
        BigDecimal monthTotal = monthCardRevenue.add(
            monthProductRevenue != null ? monthProductRevenue : BigDecimal.ZERO);
        data.put("monthRevenue", monthTotal);

        // 今日客流 + 峰值 + 销售额
        Map<String, Object> dashData = gymDailyService.getDashboardData();
        data.put("todayCount", gymDailyService.getTodayRecord());
        data.put("peakCount", dashData.get("peakCount"));
        data.put("todaySales", dashData.get("todaySales"));

        // 卡类型列表（便于前端编辑价格）
        data.put("cardTypes", membershipCardMapper.selectList(null));

        // 饼图数据：本月收入构成
        data.put("monthCardRevenue", monthCardRevenue);
        data.put("monthProductRevenue", monthProductRevenue != null ? monthProductRevenue : BigDecimal.ZERO);

        // 饼图数据：各卡类型销售数量
        Map<String, Integer> cardTypeSales = new LinkedHashMap<>();
        for (UserCard uc : allCards) {
            cardTypeSales.merge(uc.getCardName(), 1, Integer::sum);
        }
        data.put("cardTypeSales", cardTypeSales);

        // 饼图数据：卡状态分布（有效/过期）
        long activeCount = 0, expiredCount = 0;
        for (UserCard uc : allCards) {
            if (uc.getStatus() == 1 && uc.getExpireTime().isAfter(LocalDateTime.now())) {
                activeCount++;
            } else {
                expiredCount++;
            }
        }
        Map<String, Long> cardStatusDist = new LinkedHashMap<>();
        cardStatusDist.put("有效卡", activeCount);
        cardStatusDist.put("已过期", expiredCount);
        data.put("cardStatusDist", cardStatusDist);

        return Result.ok(data);
    }

    // 修改卡价格
    @PutMapping("/cards/{id}/price")
    public Result<?> updateCardPrice(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        MembershipCard card = membershipCardMapper.selectById(id);
        if (card == null) return Result.fail("卡类型不存在");
        if (body.containsKey("price")) {
            card.setPrice(new BigDecimal(body.get("price").toString()));
        }
        if (body.containsKey("name")) {
            card.setName(body.get("name").toString());
        }
        if (body.containsKey("description")) {
            card.setDescription(body.get("description").toString());
        }
        membershipCardMapper.updateById(card);
        return Result.ok("更新成功", card);
    }

    // 销售记录列表
    @GetMapping("/sales")
    public Result<?> salesList(HttpSession session,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int size) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<SalesRecord> p =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SalesRecord> wrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.orderByDesc(SalesRecord::getCreateTime);
        return Result.ok(salesRecordMapper.selectPage(p, wrapper));
    }

    // 删除销售记录
    @DeleteMapping("/sales/{id}")
    public Result<?> deleteSales(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        salesRecordMapper.deleteById(id);
        return Result.ok("删除成功", null);
    }

    // 每日营收曲线（最近14天）
    @GetMapping("/sales/daily-revenue")
    public Result<?> dailyRevenue(HttpSession session) {
        if (!isAdmin(session)) return Result.fail(403, "无权限");
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (int i = 13; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            BigDecimal revenue = salesRecordMapper.sumRevenueByDate(date);
            result.put(date.toString(), revenue != null ? revenue : BigDecimal.ZERO);
        }
        return Result.ok(result);
    }
}
