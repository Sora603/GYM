package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.SalesRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalesRecordMapper extends BaseMapper<SalesRecord> {
    @Select("SELECT COALESCE(SUM(amount), 0) FROM sales_record WHERE DATE(create_time) = #{date}")
    BigDecimal sumRevenueByDate(@Param("date") LocalDate date);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM sales_record WHERE DATE(create_time) BETWEEN #{start} AND #{end}")
    BigDecimal sumRevenueByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
