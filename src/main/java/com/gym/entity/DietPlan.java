package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("diet_plan")
public class DietPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String goalType;
    private String mealType;
    private String content;
    private String caloriesGuide;
    private String dayType;
    private String macroRatio;
    private String foodItems;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCaloriesGuide() { return caloriesGuide; }
    public void setCaloriesGuide(String caloriesGuide) { this.caloriesGuide = caloriesGuide; }
    public String getDayType() { return dayType; }
    public void setDayType(String dayType) { this.dayType = dayType; }
    public String getMacroRatio() { return macroRatio; }
    public void setMacroRatio(String macroRatio) { this.macroRatio = macroRatio; }
    public String getFoodItems() { return foodItems; }
    public void setFoodItems(String foodItems) { this.foodItems = foodItems; }
}
