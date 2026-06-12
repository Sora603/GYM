package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("user_body_info")
public class UserBodyInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal height;
    private BigDecimal weight;
    private Integer gender;
    private Integer age;
    private BigDecimal bmi;
    private BigDecimal bodyFat;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime recordTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getHeight() { return height; }
    public void setHeight(BigDecimal height) { this.height = height; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public BigDecimal getBmi() { return bmi; }
    public void setBmi(BigDecimal bmi) { this.bmi = bmi; }
    public BigDecimal getBodyFat() { return bodyFat; }
    public void setBodyFat(BigDecimal bodyFat) { this.bodyFat = bodyFat; }
    public LocalDateTime getRecordTime() { return recordTime; }
    public void setRecordTime(LocalDateTime recordTime) { this.recordTime = recordTime; }
}
