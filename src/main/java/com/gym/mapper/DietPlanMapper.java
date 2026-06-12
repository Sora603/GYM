package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.DietPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DietPlanMapper extends BaseMapper<DietPlan> {

    @Select("SELECT * FROM diet_plan WHERE goal_type = #{goalType} ORDER BY FIELD(meal_type,'BREAKFAST','LUNCH','SNACK','DINNER','BEDTIME')")
    List<DietPlan> findByGoalType(@Param("goalType") String goalType);

    @Select("SELECT * FROM diet_plan WHERE goal_type = #{goalType} AND day_type = #{dayType} ORDER BY FIELD(meal_type,'BREAKFAST','LUNCH','SNACK','DINNER','BEDTIME')")
    List<DietPlan> findByGoalTypeAndDayType(@Param("goalType") String goalType, @Param("dayType") String dayType);
}
