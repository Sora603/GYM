package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("user_checkin")
public class UserCheckin {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate checkinDate;
    private String mood;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String photo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getCheckinDate() { return checkinDate; }
    public void setCheckinDate(LocalDate checkinDate) { this.checkinDate = checkinDate; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}
