package com.erisu.cloud.megumi.pcr.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrAvatar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 头像相关
 * @Author alice
 * @Date 2021/6/22 8:44
 **/
public interface PcrAvatarMapper extends BaseMapper<PcrAvatar> {
    int insertAvatar(@Param("avatars") List<PcrAvatar> pcrAvatarList);

    PcrAvatar searchMaxStarAvatar(@Param("princessId") String princessId);
}
