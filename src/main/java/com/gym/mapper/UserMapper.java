package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM sys_user WHERE vip_card_no = #{vipCardNo}")
    User findByVipCardNo(@Param("vipCardNo") String vipCardNo);

    @Select("SELECT * FROM sys_user WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);
}
