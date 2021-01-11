package com.erisu.cloud.megumi.jap.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.jap.pojo.JapWords;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description japWordsMapper
 * @Author alice
 * @Date 2021/1/9 16:06
 **/
public interface JapWordsMapper extends BaseMapper<JapWords> {
    List<JapWords> selectRandom(@Param("count") Integer count);
}
