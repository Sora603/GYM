package com.gym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.entity.FitnessTip;
import org.apache.ibatis.annotations.Select;

public interface FitnessTipMapper extends BaseMapper<FitnessTip> {

    @Select("SELECT * FROM fitness_tip ORDER BY RAND() LIMIT 1")
    FitnessTip randomOne();
}
