package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("exercise_library")
public class ExerciseLibrary {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String category;
    private String name;
    private String emoji;
    private Integer kcalPerSet;
    private Integer restSeconds;
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public Integer getKcalPerSet() { return kcalPerSet; }
    public void setKcalPerSet(Integer kcalPerSet) { this.kcalPerSet = kcalPerSet; }
    public Integer getRestSeconds() { return restSeconds; }
    public void setRestSeconds(Integer restSeconds) { this.restSeconds = restSeconds; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
