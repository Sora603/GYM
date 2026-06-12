package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.GymDaily;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

public interface GymDailyMapper extends BaseMapper<GymDaily> {
    @Select("SELECT * FROM gym_daily WHERE record_date = #{date}")
    GymDaily findByDate(@Param("date") LocalDate date);

    @Select("SELECT * FROM gym_daily WHERE record_date BETWEEN #{start} AND #{end} ORDER BY record_date")
    List<GymDaily> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Update("UPDATE gym_daily SET current_count = current_count + 1, total_in = total_in + 1, peak_count = GREATEST(peak_count, current_count + 1) WHERE record_date = CURDATE()")
    int incrementCount();

    @Update("UPDATE gym_daily SET current_count = GREATEST(current_count - 1, 0) WHERE record_date = CURDATE()")
    int decrementCount();
}
