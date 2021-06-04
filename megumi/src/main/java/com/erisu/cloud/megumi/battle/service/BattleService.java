package com.erisu.cloud.megumi.battle.service;

import cn.hutool.core.collection.CollUtil;
import com.erisu.cloud.megumi.battle.logic.BattleLogic;
import com.erisu.cloud.megumi.battle.pojo.BattleBoss;
import com.erisu.cloud.megumi.battle.pojo.NowBoss;
import com.erisu.cloud.megumi.battle.util.BattleFormat;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @Description 公会战相关接口
 * @Author alice
 * @Date 2021/6/3 15:08
 **/
@Component
@Model(name = "gvg")
public class BattleService {
    @Resource
    private BattleLogic battleLogic;

    private final String modelName = getClass().getAnnotation(Model.class).name();

    @Command(value = "状态", commandType = CommandType.GROUP, pattern = Pattern.EQUALS,
            uuid = "637a2d00bb3d4217b60d1ad155e84d8c")
    public Message nowStage(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group group = (Group) subject;
        if (battleLogic.isEnabled(modelName, group.getId())) {
            if (battleLogic.isCreatedGroup(group.getId())) {
                // TODO: 2021/6/4
                /* 如果当前不存在boss（说明是第一次或者被clear掉了，放入一周目boss）*/
                List<NowBoss> nowBossQuery = battleLogic.getNowBossQuery(group.getId());
                if (CollUtil.isEmpty(nowBossQuery)) {
                    List<BattleBoss> bosses = battleLogic.initBoss(group.getId());
                    if (CollUtil.isEmpty(bosses)) {
                        return new PlainText("主人你写错代码了吧！快看日志");
                    } else {
                        return new PlainText(BattleFormat.INSTANCE.nowBoss(1, bosses));
                    }
                } else {
                    // 说明nowBoss表中确实有数据
                    return new PlainText(BattleFormat.INSTANCE.nowBoss1(1, nowBossQuery));
                }
            } else {
                return new PlainText("主人还没有创建工会哦");
            }
        } else {
            // TODO: 2021/6/4 公会战插件应该是默认开启的
            return new PlainText("请先启动公会战插件~");
        }
    }

    @Command(value = "创建公会", commandType = CommandType.GROUP, pattern = Pattern.EQUALS,
            uuid = "a8fe759a62004acead3091d01cb11269")
    public Message createBattleGroup(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group group = (Group) subject;
        if (battleLogic.createBattleGroup(group)) {
            // TODO: 2021/6/4 后续改用数据库存储+程序init
            File nico = ResourceUtils.getFile("classpath:emoticon/nico.jpg");
            ExternalResource externalResource = ExternalResource.create(nico);
            Image image = subject.uploadImage(externalResource);
            return new PlainText("创建工会成功").plus(image);
        } else {
            return new PlainText("创建公会失败");
        }
    }
}
