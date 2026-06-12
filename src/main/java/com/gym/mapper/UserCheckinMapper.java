package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.UserCheckin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

public interface UserCheckinMapper extends BaseMapper<UserCheckin> {

    @Select("SELECT * FROM user_checkin WHERE user_id = #{userId} AND checkin_date = #{date}")
    UserCheckin findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Select("SELECT * FROM user_checkin WHERE user_id = #{userId} AND checkin_date BETWEEN #{start} AND #{end} ORDER BY checkin_date DESC")
    List<UserCheckin> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Select("SELECT COUNT(DISTINCT user_id) FROM user_checkin WHERE checkin_date = #{date}")
    int countByDate(@Param("date") LocalDate date);

    @Select("SELECT uc.*, u.username, u.photo FROM user_checkin uc LEFT JOIN sys_user u ON uc.user_id = u.id WHERE uc.checkin_date = #{date} ORDER BY uc.create_time DESC")
    List<UserCheckin> findTodayCheckinsWithUser(@Param("date") LocalDate date);

    @Select("SELECT checkin_date FROM user_checkin WHERE user_id = #{userId} AND checkin_date BETWEEN #{start} AND #{end}")
    List<LocalDate> findCheckinDatesByUserIdAndRange(@Param("userId") Long userId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Select("SELECT COUNT(*) FROM user_checkin WHERE user_id = #{userId}")
    int countTotalByUserId(@Param("userId") Long userId);
}
