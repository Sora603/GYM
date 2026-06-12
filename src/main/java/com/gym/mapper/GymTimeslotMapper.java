package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.GymTimeslot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

public interface GymTimeslotMapper extends BaseMapper<GymTimeslot> {

    @Select("SELECT * FROM gym_timeslot WHERE record_date = #{date} ORDER BY time_slot")
    List<GymTimeslot> findByDate(@Param("date") LocalDate date);

    @Update("INSERT INTO gym_timeslot (record_date, time_slot, peak_count) VALUES (#{date}, #{slot}, #{count}) " +
            "ON DUPLICATE KEY UPDATE peak_count = GREATEST(peak_count, #{count})")
    int upsertPeak(@Param("date") LocalDate date, @Param("slot") String slot, @Param("count") int count);

    @Select("SELECT * FROM gym_timeslot WHERE record_date = #{date} AND time_slot = #{slot}")
    GymTimeslot findByDateAndSlot(@Param("date") LocalDate date, @Param("slot") String slot);
}
