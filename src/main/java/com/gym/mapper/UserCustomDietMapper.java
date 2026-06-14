package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.UserCustomDiet;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserCustomDietMapper extends BaseMapper<UserCustomDiet> {

    @Select("SELECT * FROM user_custom_diet WHERE user_id = #{userId} AND day_of_week = #{dayOfWeek}")
    java.util.List<UserCustomDiet> findByUserAndDay(@Param("userId") Long userId, @Param("dayOfWeek") Integer dayOfWeek);

    @Delete("DELETE FROM user_custom_diet WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);
}
