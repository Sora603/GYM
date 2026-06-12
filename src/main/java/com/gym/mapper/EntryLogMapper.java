package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.EntryLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface EntryLogMapper extends BaseMapper<EntryLog> {

    @Select("SELECT el.*, u.username, u.phone FROM entry_log el LEFT JOIN sys_user u ON el.user_id = u.id WHERE el.status = 1 ORDER BY el.enter_time DESC")
    List<EntryLog> findActiveEntries();

    @Select("SELECT COUNT(*) FROM entry_log WHERE user_id = #{userId} AND status = 1")
    int countActiveByUserId(@Param("userId") Long userId);

    @Update("UPDATE entry_log SET exit_time = NOW(), status = 0 WHERE id = #{id}")
    int exit(@Param("id") Long id);
}
