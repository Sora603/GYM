package com.gym.service;

import com.gym.entity.EntryLog;
import com.gym.entity.GymDaily;
import com.gym.entity.GymTimeslot;
import com.gym.entity.User;
import com.gym.entity.UserCard;
import com.gym.mapper.EntryLogMapper;
import com.gym.mapper.GymDailyMapper;
import com.gym.mapper.GymTimeslotMapper;
import com.gym.mapper.SalesRecordMapper;
import com.gym.mapper.UserCardMapper;
import com.gym.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class GymDailyService {

    @Resource
    private GymDailyMapper gymDailyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCardMapper userCardMapper;

    @Resource
    private SalesRecordMapper salesRecordMapper;

    @Resource
    private GymTimeslotMapper gymTimeslotMapper;

    @Resource
    private EntryLogMapper entryLogMapper;

    @Resource
    private CheckinService checkinService;

    // 获取今日客流信息
    public GymDaily getTodayRecord() {
        GymDaily record = gymDailyMapper.findByDate(LocalDate.now());
        if (record == null) {
            record = new GymDaily();
            record.setRecordDate(LocalDate.now());
            record.setCurrentCount(0);
            record.setTotalIn(0);
            record.setPeakCount(0);
            record.setTotalRevenue(BigDecimal.ZERO);
            record.setGymCapacity(80);
            gymDailyMapper.insert(record);
        }
        return record;
    }

    // 手机号查会员（不入场）
    public Map<String, Object> lookupByPhone(String phone) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            result.put("success", false);
            result.put("message", "未找到该手机号对应的会员");
            return result;
        }
        List<UserCard> cards = userCardMapper.findValidByUserId(user.getId());
        if (cards.isEmpty()) {
            result.put("success", false);
            result.put("message", "该用户没有有效的会员卡，请续费");
            return result;
        }
        result.put("success", true);
        result.put("user", user);
        result.put("cards", cards);
        return result;
    }

    // 手机号入场
    @Transactional
    public Map<String, Object> enterByPhone(String phone) {
        Map<String, Object> result = lookupByPhone(phone);
        if (!(boolean) result.get("success")) {
            return result;
        }

        User user = (User) result.get("user");
        // 检查是否已在场内
        if (entryLogMapper.countActiveByUserId(user.getId()) > 0) {
            result.put("success", false);
            result.put("message", user.getUsername() + " 已在场内，请勿重复入场");
            return result;
        }

        // 入场人数+1
        getTodayRecord();
        gymDailyMapper.incrementCount();

        // 更新当前时间段峰值
        updateTimeslotPeak();

        // 记录入场日志
        EntryLog entryLog = new EntryLog();
        entryLog.setUserId(user.getId());
        entryLog.setStatus(1);
        entryLogMapper.insert(entryLog);

        // 自动打卡（入场即打卡，心情默认💪）
        checkinService.doCheckin(user.getId(), "💪");

        result.put("message", "欢迎 " + user.getUsername() + " 入场！");
        return result;
    }

    // 获取仪表盘数据
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        GymDaily today = getTodayRecord();
        data.put("peakCount", today.getPeakCount() != null ? today.getPeakCount() : 0);
        data.put("currentCount", today.getCurrentCount() != null ? today.getCurrentCount() : 0);
        data.put("totalIn", today.getTotalIn() != null ? today.getTotalIn() : 0);
        // 今日销售额（卡+商品）
        BigDecimal todaySales = salesRecordMapper.sumRevenueByDate(LocalDate.now());
        data.put("todaySales", todaySales != null ? todaySales : BigDecimal.ZERO);
        return data;
    }

    // 指定人员退场
    public String exitEntry(Long entryLogId) {
        EntryLog log = entryLogMapper.selectById(entryLogId);
        if (log == null || log.getStatus() != 1) {
            return "该记录不存在或已离场";
        }
        getTodayRecord();
        gymDailyMapper.decrementCount();
        entryLogMapper.exit(entryLogId);
        return null; // 成功
    }

    // 获取当前在场人员列表
    public List<EntryLog> getActiveEntries() {
        return entryLogMapper.findActiveEntries();
    }

    // 手动减人数（保留兼容）
    public void decrementCount() {
        getTodayRecord();
        gymDailyMapper.decrementCount();
        // 同时关闭最近一条未离场记录
        List<EntryLog> active = entryLogMapper.findActiveEntries();
        if (!active.isEmpty()) {
            entryLogMapper.exit(active.get(0).getId());
        }
    }

    // 获取某月每日峰值
    public List<GymDaily> getMonthPeaks(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return gymDailyMapper.findByDateRange(start, end);
    }

    // 判断当前时间段
    private String getCurrentTimeSlot() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour >= 8 && hour < 10) return "8:00-10:00";
        if (hour >= 10 && hour < 12) return "10:00-12:00";
        if (hour >= 12 && hour < 14) return "12:00-14:00";
        if (hour >= 14 && hour < 16) return "14:00-16:00";
        if (hour >= 16 && hour < 20) return "16:00-20:00";
        if (hour >= 20 && hour < 22) return "20:00-22:00";
        return "其他";
    }

    // 更新当前时间段峰值
    private void updateTimeslotPeak() {
        String slot = getCurrentTimeSlot();
        GymDaily today = getTodayRecord();
        int currentCount = today.getCurrentCount() != null ? today.getCurrentCount() : 0;
        gymTimeslotMapper.upsertPeak(LocalDate.now(), slot, currentCount);
    }

    // 获取今日各时间段峰值
    public List<GymTimeslot> getTodayTimeslots() {
        return gymTimeslotMapper.findByDate(LocalDate.now());
    }

    // 获取指定日期各时间段峰值
    public List<GymTimeslot> getTimeslotsByDate(LocalDate date) {
        return gymTimeslotMapper.findByDate(date);
    }
}
