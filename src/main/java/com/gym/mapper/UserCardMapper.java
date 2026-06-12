package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.UserCard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserCardMapper extends BaseMapper<UserCard> {
    @Select("SELECT uc.*, mc.type AS card_type FROM user_card uc LEFT JOIN membership_card mc ON uc.card_id = mc.id WHERE uc.user_id = #{userId} ORDER BY uc.buy_time DESC")
    List<UserCard> findByUserId(@Param("userId") Long userId);

    @Select("SELECT uc.*, mc.type AS card_type FROM user_card uc LEFT JOIN membership_card mc ON uc.card_id = mc.id WHERE uc.user_id = #{userId} AND uc.status = 1 AND uc.expire_time > NOW() ORDER BY uc.expire_time ASC")
    List<UserCard> findValidByUserId(@Param("userId") Long userId);
}
