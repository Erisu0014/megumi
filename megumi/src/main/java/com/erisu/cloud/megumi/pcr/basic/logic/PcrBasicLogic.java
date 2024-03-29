package com.erisu.cloud.megumi.pcr.basic.logic;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description pcr基础数据初始化
 * @Author alice
 * @Date 2021/6/22 9:16
 **/
@Component
public class PcrBasicLogic implements ApplicationRunner {
    //    @Value("${basicUrl}")
//    private String basicUrl;
//    @Resource
//    private PcrAvatarMapper avatarMapper;
    @Resource
    private PcrInitData pcrInitData;

    @Override
    public void run(ApplicationArguments args) {
        pcrInitData.initData();
//        List<PcrAvatar> avatarList = pcrInitData.getAvatarList(basicUrl + "/avatar");
//        avatarMapper.insertAvatar(avatarList);
    }
}
