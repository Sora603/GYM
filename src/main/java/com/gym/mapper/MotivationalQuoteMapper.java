package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.MotivationalQuote;
import org.apache.ibatis.annotations.Select;

public interface MotivationalQuoteMapper extends BaseMapper<MotivationalQuote> {

    @Select("SELECT * FROM motivational_quote ORDER BY RAND() LIMIT 1")
    MotivationalQuote randomOne();
}
