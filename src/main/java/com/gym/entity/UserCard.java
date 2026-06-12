package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("user_card")
public class UserCard {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long cardId;
    private String cardName;
    private LocalDateTime buyTime;
    private LocalDateTime expireTime;
    private Integer status;
    @TableField(exist = false)
    private String cardType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }
    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }
    public LocalDateTime getBuyTime() { return buyTime; }
    public void setBuyTime(LocalDateTime buyTime) { this.buyTime = buyTime; }
    public LocalDateTime getExpireTime() { return expireTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
}
