package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gym.entity.*;
import com.gym.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomPlanService {

    @Resource
    private UserCustomTrainingMapper trainingMapper;

    @Resource
    private UserCustomDietMapper dietMapper;

    @Resource
    private TrainingPlanMapper trainingPlanMapper;

    @Resource
    private DietPlanMapper dietPlanMapper;

    @Resource
    private FoodLibraryMapper foodLibraryMapper;

    @Resource
    private ExerciseLibraryMapper exerciseLibraryMapper;

    private static final String[] WEEK_NAMES = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static final String[] WEEK_SHORT = {"", "一", "二", "三", "四", "五", "六", "日"};

    /**
     * 获取用户的7天自定义训练计划
     */
    public List<Map<String, Object>> getCustomTraining(Long userId) {
        List<UserCustomTraining> list = trainingMapper.selectList(
            new QueryWrapper<UserCustomTraining>().eq("user_id", userId).orderByAsc("day_of_week"));

        // 如果用户还没有自定义计划，返回空模板
        if (list.isEmpty()) return buildEmptyTrainingTemplate();

        List<Map<String, Object>> result = new ArrayList<>();
        for (UserCustomTraining t : list) {
            result.add(trainingToMap(t));
        }
        return result;
    }

    /**
     * 保存某一天的自定义训练
     */
    @Transactional
    public void saveCustomTraining(Long userId, Map<String, Object> body) {
        Integer dayOfWeek = (Integer) body.get("dayOfWeek");
        UserCustomTraining existing = trainingMapper.findByUserAndDay(userId, dayOfWeek);

        if (existing == null) {
            existing = new UserCustomTraining();
            existing.setUserId(userId);
            existing.setDayOfWeek(dayOfWeek);
        }
        existing.setFocus((String) body.getOrDefault("focus", ""));
        existing.setDayType((String) body.getOrDefault("dayType", "STRENGTH"));
        existing.setExercises((String) body.getOrDefault("exercises", "[]"));
        existing.setWarmUp((String) body.getOrDefault("warmUp", null));
        existing.setCardio((String) body.getOrDefault("cardio", null));
        existing.setNotes((String) body.getOrDefault("notes", null));
        if (existing.getId() == null) {
            trainingMapper.insert(existing);
        } else {
            trainingMapper.updateById(existing);
        }
    }

    /**
     * 保存某一天的自定义饮食
     */
    @Transactional
    public void saveCustomDiet(Long userId, Map<String, Object> body) {
        Integer dayOfWeek = (Integer) body.get("dayOfWeek");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> meals = (List<Map<String, String>>) body.get("meals");

        // 删除当天已有饮食
        List<UserCustomDiet> existingList = dietMapper.findByUserAndDay(userId, dayOfWeek);
        for (UserCustomDiet d : existingList) {
            dietMapper.deleteById(d.getId());
        }

        if (meals != null) {
            for (Map<String, String> meal : meals) {
                UserCustomDiet d = new UserCustomDiet();
                d.setUserId(userId);
                d.setDayOfWeek(dayOfWeek);
                d.setMealType(meal.get("mealType"));
                d.setContent(meal.get("content"));
                d.setCaloriesGuide(meal.get("caloriesGuide"));
                d.setMacroRatio(meal.get("macroRatio"));
                d.setFoodItems(meal.get("foodItems"));
                dietMapper.insert(d);
            }
        }
    }

    /**
     * 获取用户7天自定义饮食
     */
    public Map<Integer, List<Map<String, String>>> getCustomDiet(Long userId) {
        List<UserCustomDiet> all = dietMapper.selectList(
            new QueryWrapper<UserCustomDiet>().eq("user_id", userId).orderByAsc("day_of_week"));

        Map<Integer, List<Map<String, String>>> result = new LinkedHashMap<>();
        for (int d = 1; d <= 7; d++) result.put(d, new ArrayList<>());

        for (UserCustomDiet d : all) {
            Map<String, String> m = new HashMap<>();
            m.put("mealType", d.getMealType());
            m.put("content", d.getContent());
            m.put("caloriesGuide", d.getCaloriesGuide());
            m.put("macroRatio", d.getMacroRatio());
            m.put("foodItems", d.getFoodItems());
            result.get(d.getDayOfWeek()).add(m);
        }
        return result;
    }

    /**
     * 从系统模板重置自定义计划
     */
    @Transactional
    public void resetFromTemplate(Long userId, String goalType) {
        // 清空现有
        trainingMapper.deleteByUserId(userId);
        dietMapper.deleteByUserId(userId);

        // 复制训练模板
        List<TrainingPlan> trainings = trainingPlanMapper.findByGoalType(goalType);
        for (TrainingPlan t : trainings) {
            UserCustomTraining ct = new UserCustomTraining();
            ct.setUserId(userId);
            ct.setDayOfWeek(t.getDayOfWeek());
            ct.setFocus(t.getFocus());
            ct.setDayType(t.getDayType() != null ? t.getDayType() : "STRENGTH");
            ct.setExercises(t.getExercises());
            ct.setWarmUp(t.getWarmUp());
            ct.setCardio(t.getCardio());
            ct.setNotes(t.getNotes());
            trainingMapper.insert(ct);
        }

        // 饮食部分用户通过食物选择器自行搭配
    }

    /**
     * 一键清空自定义计划
     */
    @Transactional
    public void clearAll(Long userId) {
        trainingMapper.deleteByUserId(userId);
        dietMapper.deleteByUserId(userId);
    }

    /**
     * 获取食物库
     */
    public List<FoodLibrary> getFoodLibrary() {
        return foodLibraryMapper.selectList(null);
    }

    /**
     * 获取动作库
     */
    public List<ExerciseLibrary> getExerciseLibrary() {
        return exerciseLibraryMapper.selectList(null);
    }

    private Map<String, Object> trainingToMap(UserCustomTraining t) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("dayOfWeek", t.getDayOfWeek());
        item.put("dayName", WEEK_NAMES[t.getDayOfWeek()]);
        item.put("dayShort", WEEK_SHORT[t.getDayOfWeek()]);
        item.put("dayType", t.getDayType() != null ? t.getDayType() : "STRENGTH");
        item.put("focus", t.getFocus());
        item.put("warmUp", t.getWarmUp());
        item.put("cardio", t.getCardio());
        item.put("notes", t.getNotes());
        item.put("exercises", parseExercises(t.getExercises()));
        return item;
    }

    private List<Map<String, Object>> buildEmptyTrainingTemplate() {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> empty = new LinkedHashMap<>();
        empty.put("name", "");
        empty.put("sets", "");
        empty.put("reps", "");
        empty.put("rest", "");
        empty.put("note", "");
        result.add(empty);
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> parseExercises(String json) {
        List<Map<String, String>> list = new ArrayList<>();
        if (json == null || json.trim().isEmpty() || "[]".equals(json.trim())) return list;
        try {
            String content = json.trim();
            if (content.startsWith("[")) content = content.substring(1, content.length() - 1);
            String[] parts = content.split("\\},\\{");
            for (String part : parts) {
                part = part.replace("{", "").replace("}", "").replace("\"", "");
                Map<String, String> map = new LinkedHashMap<>();
                String[] fields = part.split(",");
                for (String field : fields) {
                    String[] kv = field.split(":", 2);
                    if (kv.length == 2) {
                        map.put(kv[0].trim(), kv[1].trim());
                    }
                }
                if (!map.isEmpty()) list.add(map);
            }
        } catch (Exception e) { /* ignore */ }
        return list;
    }
}
