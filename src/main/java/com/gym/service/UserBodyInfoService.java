package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.entity.UserBodyInfo;
import com.gym.mapper.UserBodyInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserBodyInfoService {

    @Resource
    private UserBodyInfoMapper userBodyInfoMapper;

    /**
     * 保存身体数据并自动计算BMI和体脂率
     */
    public UserBodyInfo save(Long userId, BigDecimal height, BigDecimal weight, Integer gender, Integer age) {
        if (height == null || weight == null || gender == null || age == null) {
            throw new RuntimeException("身高、体重、性别、年龄不能为空");
        }
        if (height.compareTo(BigDecimal.ZERO) <= 0 || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("身高和体重必须大于0");
        }

        // BMI = 体重(kg) / (身高(m))²
        BigDecimal heightInMeters = height.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
        BigDecimal bmi = weight.divide(heightInMeters.multiply(heightInMeters), 1, RoundingMode.HALF_UP);

        // Deurenberg体脂率公式: BF% = 1.20 × BMI + 0.23 × Age - 10.8 × Gender - 5.4
        // Gender: 男=1, 女=0
        BigDecimal bodyFat = new BigDecimal("1.20").multiply(bmi)
                .add(new BigDecimal("0.23").multiply(new BigDecimal(age)))
                .subtract(new BigDecimal("10.8").multiply(new BigDecimal(gender)))
                .subtract(new BigDecimal("5.4"))
                .setScale(1, RoundingMode.HALF_UP);

        UserBodyInfo info = new UserBodyInfo();
        info.setUserId(userId);
        info.setHeight(height);
        info.setWeight(weight);
        info.setGender(gender);
        info.setAge(age);
        info.setBmi(bmi);
        info.setBodyFat(bodyFat);
        info.setRecordTime(LocalDateTime.now());
        userBodyInfoMapper.insert(info);
        return info;
    }

    /**
     * 获取用户最新一次身体数据
     */
    public UserBodyInfo getLatest(Long userId) {
        return userBodyInfoMapper.findLatestByUserId(userId);
    }

    /**
     * 获取用户历史身体数据
     */
    public List<UserBodyInfo> getHistory(Long userId) {
        return userBodyInfoMapper.findByUserId(userId);
    }

    /**
     * 管理员直接删除（不校验userId）
     */
    public void deleteById(Long id) {
        userBodyInfoMapper.deleteById(id);
    }

    /**
     * 删除某条记录
     */
    public void deleteRecord(Long id, Long userId) {
        UserBodyInfo record = userBodyInfoMapper.selectById(id);
        if (record == null || !record.getUserId().equals(userId)) {
            throw new RuntimeException("记录不存在或无权操作");
        }
        userBodyInfoMapper.deleteById(id);
    }

    /**
     * 管理员查看所有会员最新身体数据
     */
    public List<Map<String, Object>> getAllMembersLatest() {
        List<UserBodyInfo> list = userBodyInfoMapper.findLatestGroupByUser();
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (UserBodyInfo info : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", info.getId());
            map.put("userId", info.getUserId());
            map.put("height", info.getHeight());
            map.put("weight", info.getWeight());
            map.put("bmi", info.getBmi());
            map.put("bodyFat", info.getBodyFat());
            map.put("recordTime", info.getRecordTime());
            result.add(map);
        }
        return result;
    }
}
