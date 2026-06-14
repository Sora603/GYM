package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("food_library")
public class FoodLibrary {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String category;
    private String name;
    private String unit;
    private Integer kcalPerUnit;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private String emoji;
    private Integer baseGrams;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Integer getKcalPerUnit() { return kcalPerUnit; }
    public void setKcalPerUnit(Integer kcalPerUnit) { this.kcalPerUnit = kcalPerUnit; }
    public Double getProteinG() { return proteinG; }
    public void setProteinG(Double proteinG) { this.proteinG = proteinG; }
    public Double getCarbsG() { return carbsG; }
    public void setCarbsG(Double carbsG) { this.carbsG = carbsG; }
    public Double getFatG() { return fatG; }
    public void setFatG(Double fatG) { this.fatG = fatG; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public Integer getBaseGrams() { return baseGrams; }
    public void setBaseGrams(Integer baseGrams) { this.baseGrams = baseGrams; }
}
