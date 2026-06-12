package com.gym.service;

import com.gym.entity.*;
import com.gym.mapper.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

@Service
public class CheckinService {

    @Resource
    private UserCheckinMapper checkinMapper;

    @Resource
    private MotivationalQuoteMapper quoteMapper;

    @Resource
    private FitnessTipMapper fitnessTipMapper;

    @Resource
    private com.gym.mapper.UserBodyInfoMapper userBodyInfoMapper;

    public Map<String, Object> doCheckin(Long userId, String mood) {
        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();

        UserCheckin existing = checkinMapper.findByUserIdAndDate(userId, today);
        if (existing != null) {
            result.put("success", false);
            result.put("message", "今日已打卡，明天继续加油！");
            return result;
        }

        UserCheckin checkin = new UserCheckin();
        checkin.setUserId(userId);
        checkin.setCheckinDate(today);
        checkin.setMood(mood);
        checkinMapper.insert(checkin);

        MotivationalQuote quote = quoteMapper.randomOne();
        Map<String, String> quoteData = new HashMap<>();
        if (quote != null) {
            quoteData.put("content", quote.getContent());
            quoteData.put("author", quote.getAuthor() != null ? quote.getAuthor() : "");
        } else {
            quoteData.put("content", "坚持就是胜利！");
            quoteData.put("author", "");
        }

        result.put("success", true);
        result.put("message", "打卡成功！");
        result.put("quote", quoteData);
        result.put("streak", calcStreak(userId, today));
        return result;
    }

    private int calcStreak(Long userId, LocalDate today) {
        int streak = 0;
        LocalDate date = today;
        while (checkinMapper.findByUserIdAndDate(userId, date) != null) {
            streak++;
            date = date.minusDays(1);
        }
        return streak;
    }

    /**
     * 获取用户打卡状态——全量数据一锅出
     */
    public Map<String, Object> getStatus(Long userId) {
        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();
        int streak = calcStreak(userId, today);
        int totalCount = checkinMapper.countTotalByUserId(userId);
        LocalDate monthStart = today.withDayOfMonth(1);

        // 基础状态
        UserCheckin todayCheckin = checkinMapper.findByUserIdAndDate(userId, today);
        result.put("checkedToday", todayCheckin != null);
        result.put("todayMood", todayCheckin != null ? todayCheckin.getMood() : null);
        result.put("streak", streak);
        result.put("monthCount", checkinMapper.findByUserIdAndDateRange(userId, monthStart, today).size());
        result.put("totalCount", totalCount);

        // 本月打卡日期
        List<LocalDate> dates = checkinMapper.findCheckinDatesByUserIdAndRange(userId, monthStart, today);
        List<String> dateStrList = new ArrayList<>();
        for (LocalDate d : dates) dateStrList.add(d.toString());
        result.put("monthDates", dateStrList);

        // 成就徽章
        result.put("badges", calcBadges(streak, totalCount));

        // 本周回顾：周一到周日 [ {dayShort, date, checked, mood} ]
        result.put("weekReview", buildWeekReview(userId, today));

        // 最近打卡时间轴（最近7条）
        result.put("recentTimeline", buildTimeline(userId));

        // 今日全站打卡人数
        result.put("todayTotalCount", checkinMapper.countByDate(today));

        // 个人纪录
        result.put("personalRecords", calcPersonalRecords(userId, today));

        // 每日小贴士
        FitnessTip tip = fitnessTipMapper.randomOne();
        if (tip != null) {
            Map<String, String> tipData = new HashMap<>();
            tipData.put("content", tip.getContent());
            tipData.put("category", tip.getCategory());
            result.put("dailyTip", tipData);
        }

        // 最新身体数据
        UserBodyInfo latestBody = userBodyInfoMapper.findLatestByUserId(userId);
        if (latestBody != null) {
            Map<String, Object> bodyData = new HashMap<>();
            bodyData.put("height", latestBody.getHeight());
            bodyData.put("weight", latestBody.getWeight());
            bodyData.put("bmi", latestBody.getBmi());
            bodyData.put("bodyFat", latestBody.getBodyFat());
            bodyData.put("recordTime", latestBody.getRecordTime() != null ? latestBody.getRecordTime().toString() : null);
            result.put("latestBody", bodyData);
        }

        return result;
    }

    /**
     * 计算成就徽章
     */
    private List<Map<String, Object>> calcBadges(int streak, int totalCount) {
        List<Map<String, Object>> badges = new ArrayList<>();
        int[][] thresholds = {{7, 7}, {14, 14}, {21, 21}, {30, 30}, {60, 60}, {100, 100}};
        String[] icons = {"🔥", "⭐", "💎", "👑", "🏅", "🏆"};
        String[] names = {"7天连续", "14天连续", "21天连续", "30天连续", "60天连续", "100天累计"};
        for (int i = 0; i < thresholds.length; i++) {
            Map<String, Object> b = new HashMap<>();
            b.put("icon", icons[i]);
            b.put("name", names[i]);
            b.put("target", thresholds[i][1]);
            if (i < 5) {
                b.put("unlocked", streak >= thresholds[i][0]);
                b.put("progress", Math.min(streak, thresholds[i][0]));
            } else {
                b.put("unlocked", totalCount >= thresholds[i][0]);
                b.put("progress", Math.min(totalCount, thresholds[i][0]));
            }
            badges.add(b);
        }
        return badges;
    }

    /**
     * 本周回顾（周一到周日）
     */
    private List<Map<String, Object>> buildWeekReview(Long userId, LocalDate today) {
        List<Map<String, Object>> week = new ArrayList<>();
        java.time.DayOfWeek dow = today.getDayOfWeek();
        int daysFromMonday = dow.getValue() - 1; // Monday=0
        LocalDate monday = today.minusDays(daysFromMonday);
        String[] dayShorts = {"一", "二", "三", "四", "五", "六", "日"};

        for (int i = 0; i < 7; i++) {
            LocalDate d = monday.plusDays(i);
            UserCheckin c = checkinMapper.findByUserIdAndDate(userId, d);
            Map<String, Object> item = new HashMap<>();
            item.put("dayShort", dayShorts[i]);
            item.put("date", d.toString());
            item.put("checked", c != null);
            item.put("mood", c != null ? c.getMood() : null);
            item.put("isToday", d.equals(today));
            item.put("isFuture", d.isAfter(today));
            week.add(item);
        }
        return week;
    }

    /**
     * 最近打卡时间轴
     */
    private List<Map<String, Object>> buildTimeline(Long userId) {
        List<Map<String, Object>> timeline = new ArrayList<>();
        LocalDate today = LocalDate.now();
        List<UserCheckin> recent = checkinMapper.findByUserIdAndDateRange(
            userId, today.minusDays(30), today);
        int count = 0;
        for (UserCheckin c : recent) {
            if (count >= 7) break;
            Map<String, Object> item = new HashMap<>();
            item.put("date", c.getCheckinDate().toString());
            item.put("mood", c.getMood());
            item.put("time", c.getCreateTime() != null ?
                c.getCreateTime().toLocalTime().toString().substring(0, 5) : "");
            timeline.add(item);
            count++;
        }
        return timeline;
    }

    /**
     * 个人纪录
     */
    private Map<String, Object> calcPersonalRecords(Long userId, LocalDate today) {
        Map<String, Object> records = new HashMap<>();

        // 最长连续打卡
        int maxStreak = 0, currentStreak = 0;
        LocalDate cursor = today.minusDays(365);
        while (!cursor.isAfter(today)) {
            if (checkinMapper.findByUserIdAndDate(userId, cursor) != null) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
            cursor = cursor.plusDays(1);
        }
        records.put("maxStreak", maxStreak);

        // 单月最多打卡
        int maxMonth = 0;
        for (int m = 1; m <= 12; m++) {
            LocalDate ms = LocalDate.of(today.getYear(), m, 1);
            if (ms.isAfter(today)) break;
            LocalDate me = ms.plusMonths(1).minusDays(1);
            if (me.isAfter(today)) me = today;
            int cnt = checkinMapper.findCheckinDatesByUserIdAndRange(userId, ms, me).size();
            if (cnt > maxMonth) maxMonth = cnt;
        }
        records.put("maxMonth", maxMonth);

        // 本周打卡天数
        java.time.DayOfWeek dow = today.getDayOfWeek();
        LocalDate monday = today.minusDays(dow.getValue() - 1);
        int weekCount = checkinMapper.findCheckinDatesByUserIdAndRange(userId, monday, today).size();
        records.put("weekCount", weekCount);

        return records;
    }

    public Map<String, Object> getYearCheckins(Long userId, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<LocalDate> dates = checkinMapper.findCheckinDatesByUserIdAndRange(userId, start, end);
        List<String> dateStrList = new ArrayList<>();
        for (LocalDate d : dates) dateStrList.add(d.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("dates", dateStrList);
        return result;
    }

    public Map<String, Object> getMonthCheckins(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        List<LocalDate> dates = checkinMapper.findCheckinDatesByUserIdAndRange(userId, start, end);
        List<String> dateStrList = new ArrayList<>();
        for (LocalDate d : dates) dateStrList.add(d.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("dates", dateStrList);
        return result;
    }

    public Map<String, Object> getTodayOverview() {
        LocalDate today = LocalDate.now();
        Map<String, Object> result = new HashMap<>();
        result.put("date", today.toString());
        result.put("count", checkinMapper.countByDate(today));
        result.put("list", checkinMapper.findTodayCheckinsWithUser(today));
        return result;
    }

    public List<Map<String, Object>> getCheckinTrend(int days) {
        List<Map<String, Object>> trend = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            Map<String, Object> item = new HashMap<>();
            item.put("date", date.toString());
            item.put("count", checkinMapper.countByDate(date));
            trend.add(item);
        }
        return trend;
    }
}
