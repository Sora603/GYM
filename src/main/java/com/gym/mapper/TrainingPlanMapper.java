package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.TrainingPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TrainingPlanMapper extends BaseMapper<TrainingPlan> {

    @Select("SELECT * FROM training_plan WHERE goal_type = #{goalType} ORDER BY day_of_week")
    List<TrainingPlan> findByGoalType(@Param("goalType") String goalType);
}
