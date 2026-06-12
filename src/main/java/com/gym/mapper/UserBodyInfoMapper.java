package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.UserBodyInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserBodyInfoMapper extends BaseMapper<UserBodyInfo> {

    @Select("SELECT * FROM user_body_info WHERE user_id = #{userId} ORDER BY record_time DESC")
    List<UserBodyInfo> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_body_info WHERE user_id = #{userId} ORDER BY record_time DESC LIMIT 1")
    UserBodyInfo findLatestByUserId(@Param("userId") Long userId);

    @Select("SELECT ubi.*, u.username FROM user_body_info ubi " +
            "INNER JOIN sys_user u ON ubi.user_id = u.id " +
            "WHERE ubi.record_time = (SELECT MAX(ubi2.record_time) FROM user_body_info ubi2 WHERE ubi2.user_id = ubi.user_id) " +
            "ORDER BY ubi.record_time DESC")
    List<UserBodyInfo> findLatestGroupByUser();
}
