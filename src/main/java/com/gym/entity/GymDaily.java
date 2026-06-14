package com.gym.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("gym_daily")
public class GymDaily {
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate recordDate;
    private Integer currentCount;
    private Integer totalIn;
    private Integer peakCount;
    private java.math.BigDecimal totalRevenue;
    private Integer gymCapacity;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    public Integer getCurrentCount() { return currentCount; }
    public void setCurrentCount(Integer currentCount) { this.currentCount = currentCount; }
    public Integer getTotalIn() { return totalIn; }
    public void setTotalIn(Integer totalIn) { this.totalIn = totalIn; }
    public Integer getPeakCount() { return peakCount; }
    public void setPeakCount(Integer peakCount) { this.peakCount = peakCount; }
    public java.math.BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(java.math.BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public Integer getGymCapacity() { return gymCapacity; }
    public void setGymCapacity(Integer gymCapacity) { this.gymCapacity = gymCapacity; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
