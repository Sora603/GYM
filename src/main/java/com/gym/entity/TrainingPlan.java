package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("training_plan")
public class TrainingPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String goalType;
    private Integer dayOfWeek;
    private String focus;
    private String exercises;
    private String warmUp;
    private String cardio;
    private String notes;
    private String dayType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getFocus() { return focus; }
    public void setFocus(String focus) { this.focus = focus; }
    public String getExercises() { return exercises; }
    public void setExercises(String exercises) { this.exercises = exercises; }
    public String getWarmUp() { return warmUp; }
    public void setWarmUp(String warmUp) { this.warmUp = warmUp; }
    public String getCardio() { return cardio; }
    public void setCardio(String cardio) { this.cardio = cardio; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDayType() { return dayType; }
    public void setDayType(String dayType) { this.dayType = dayType; }
}
