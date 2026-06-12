package com.gym.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.entity.Coach;
import com.gym.mapper.CoachMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CoachService {

    @Resource
    private CoachMapper coachMapper;

    // 获取在职教练列表
    public List<Coach> getActiveCoaches() {
        LambdaQueryWrapper<Coach> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coach::getStatus, 1);
        return coachMapper.selectList(wrapper);
    }

    // 获取全部教练
    public List<Coach> getAllCoaches() {
        return coachMapper.selectList(null);
    }

    // 添加教练
    public Coach addCoach(Coach coach) {
        coachMapper.insert(coach);
        return coach;
    }

    // 更新教练
    public Coach updateCoach(Coach coach) {
        coachMapper.updateById(coach);
        return coachMapper.selectById(coach.getId());
    }

    // 删除教练（软删除，改状态）
    public void deleteCoach(Long id) {
        Coach coach = coachMapper.selectById(id);
        if (coach != null) {
            coach.setStatus(0);
            coachMapper.updateById(coach);
        }
    }

    public Coach getById(Long id) {
        return coachMapper.selectById(id);
    }
}
