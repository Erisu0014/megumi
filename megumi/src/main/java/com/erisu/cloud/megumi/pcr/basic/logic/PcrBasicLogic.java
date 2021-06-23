package com.erisu.cloud.megumi.pcr.basic.logic;

import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper;
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrAvatar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description pcr基础数据初始化
 * @Author alice
 * @Date 2021/6/22 9:16
 **/
@Component
public class PcrBasicLogic implements ApplicationRunner {
    @Value("${basicUrl}")
    private String basicUrl;
    @Resource
    private PcrAvatarMapper avatarMapper;
    @Resource
    private PcrInitData pcrInitData;

    @Override
    public void run(ApplicationArguments args) {
        List<PcrAvatar> avatarList = pcrInitData.getAvatarList(basicUrl + "/avatar");
        avatarMapper.insertAvatar(avatarList);
    }
}
