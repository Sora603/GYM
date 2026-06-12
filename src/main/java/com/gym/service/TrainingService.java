package com.gym.service;

import com.gym.entity.DietPlan;
import com.gym.entity.TrainingPlan;
import com.gym.mapper.DietPlanMapper;
import com.gym.mapper.TrainingPlanMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TrainingService {

    @Resource
    private TrainingPlanMapper trainingPlanMapper;

    @Resource
    private DietPlanMapper dietPlanMapper;

    private static final String[] WEEK_NAMES = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static final String[] WEEK_SHORT = {"", "一", "二", "三", "四", "五", "六", "日"};

    private static final Map<String, String> MEAL_NAMES = new LinkedHashMap<>();
    static {
        MEAL_NAMES.put("BREAKFAST", "早餐");
        MEAL_NAMES.put("LUNCH", "午餐");
        MEAL_NAMES.put("SNACK", "加餐");
        MEAL_NAMES.put("DINNER", "晚餐");
        MEAL_NAMES.put("BEDTIME", "睡前加餐");
    }

    private static final Map<String, String> MEAL_EMOJI = new LinkedHashMap<>();
    static {
        MEAL_EMOJI.put("BREAKFAST", "🥣");
        MEAL_EMOJI.put("LUNCH", "🍗");
        MEAL_EMOJI.put("SNACK", "🍌");
        MEAL_EMOJI.put("DINNER", "🐟");
        MEAL_EMOJI.put("BEDTIME", "🥛");
    }

    /**
     * 获取完整训练计划（训练+按日类型分组的饮食）
     */
    public Map<String, Object> getFullPlan(String goalType) {
        Map<String, Object> result = new HashMap<>();
        result.put("goalType", goalType);

        // 训练计划——每个训练日带 dayType
        List<TrainingPlan> trainings = trainingPlanMapper.findByGoalType(goalType);
        List<Map<String, Object>> trainingList = new ArrayList<>();
        for (TrainingPlan t : trainings) {
            Map<String, Object> item = new HashMap<>();
            item.put("dayOfWeek", t.getDayOfWeek());
            item.put("dayName", WEEK_NAMES[t.getDayOfWeek()]);
            item.put("dayShort", WEEK_SHORT[t.getDayOfWeek()]);
            item.put("dayType", t.getDayType() != null ? t.getDayType() : "STRENGTH");
            item.put("focus", t.getFocus());
            item.put("warmUp", t.getWarmUp());
            item.put("cardio", t.getCardio());
            item.put("notes", t.getNotes());
            item.put("exercises", parseExercises(t.getExercises()));
            trainingList.add(item);
        }
        result.put("trainings", trainingList);

        // 饮食计划——按 dayType 分组返回，前端根据当天 dayType 匹配
        String[] dayTypes = {"STRENGTH", "CARDIO", "REST"};
        Map<String, List<Map<String, String>>> dietsByDayType = new LinkedHashMap<>();
        for (String dt : dayTypes) {
            List<DietPlan> diets = dietPlanMapper.findByGoalTypeAndDayType(goalType, dt);
            if (diets.isEmpty()) {
                // 没有对应 dayType 的饮食，回退到 STRENGTH 类型
                diets = dietPlanMapper.findByGoalTypeAndDayType(goalType, "STRENGTH");
            }
            List<Map<String, String>> mealList = new ArrayList<>();
            for (DietPlan d : diets) {
                Map<String, String> meal = new HashMap<>();
                meal.put("mealType", d.getMealType());
                meal.put("mealName", MEAL_NAMES.getOrDefault(d.getMealType(), d.getMealType()));
                meal.put("emoji", MEAL_EMOJI.getOrDefault(d.getMealType(), "🍽️"));
                meal.put("content", d.getContent());
                meal.put("caloriesGuide", d.getCaloriesGuide());
                meal.put("macroRatio", d.getMacroRatio());
                meal.put("foodItems", d.getFoodItems());
                mealList.add(meal);
            }
            dietsByDayType.put(dt, mealList);
        }
        result.put("dietsByDayType", dietsByDayType);

        // 日类型中文标签
        Map<String, String> dayTypeLabels = new LinkedHashMap<>();
        dayTypeLabels.put("STRENGTH", "力量训练日");
        dayTypeLabels.put("CARDIO", "有氧燃脂日");
        dayTypeLabels.put("REST", "休息恢复日");
        result.put("dayTypeLabels", dayTypeLabels);

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> parseExercises(String json) {
        List<Map<String, String>> list = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) return list;
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
        } catch (Exception e) {
            // ignore
        }
        return list;
    }
}
