package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("entry_log")
public class EntryLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long cardId;
    private LocalDateTime enterTime;
    private LocalDateTime exitTime;
    private Integer status;

    // 仅用于展示，非DB字段
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String phone;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }
    public LocalDateTime getEnterTime() { return enterTime; }
    public void setEnterTime(LocalDateTime enterTime) { this.enterTime = enterTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
