package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.UserCustomTraining;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserCustomTrainingMapper extends BaseMapper<UserCustomTraining> {

    @Select("SELECT * FROM user_custom_training WHERE user_id = #{userId} AND day_of_week = #{dayOfWeek}")
    UserCustomTraining findByUserAndDay(@Param("userId") Long userId, @Param("dayOfWeek") Integer dayOfWeek);

    @Delete("DELETE FROM user_custom_training WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);
}
