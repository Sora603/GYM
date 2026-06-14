package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("user_custom_diet")
public class UserCustomDiet {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer dayOfWeek;
    private String mealType;
    private String content;
    private String caloriesGuide;
    private String macroRatio;
    private String foodItems;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCaloriesGuide() { return caloriesGuide; }
    public void setCaloriesGuide(String caloriesGuide) { this.caloriesGuide = caloriesGuide; }
    public String getMacroRatio() { return macroRatio; }
    public void setMacroRatio(String macroRatio) { this.macroRatio = macroRatio; }
    public String getFoodItems() { return foodItems; }
    public void setFoodItems(String foodItems) { this.foodItems = foodItems; }
}
