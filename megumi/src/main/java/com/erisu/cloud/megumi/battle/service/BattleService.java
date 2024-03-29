//package com.erisu.cloud.megumi.battle.service;
//
//import com.erisu.cloud.megumi.battle.logic.BattleLogic;
//import com.erisu.cloud.megumi.command.Command;
//import com.erisu.cloud.megumi.command.CommandType;
//import com.erisu.cloud.megumi.pattern.Pattern;
//import com.erisu.cloud.megumi.plugin.pojo.Model;
//import net.mamoe.mirai.contact.Contact;
//import net.mamoe.mirai.contact.Group;
//import net.mamoe.mirai.contact.User;
//import net.mamoe.mirai.message.data.Message;
//import net.mamoe.mirai.message.data.MessageChain;
//import net.mamoe.mirai.message.data.PlainText;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @Description 公会战相关接口
// * @Author alice
// * @Date 2021/6/3 15:08
// **/
//@Component
//@Model(name = "公会战",help="暂时停用")
//public class BattleService {
//    @Resource
//    private BattleLogic battleLogic;
//
//    private final String modelName = getClass().getAnnotation(Model.class).name();
//
////    @Command(value = "状态", commandType = CommandType.GROUP, pattern = Pattern.EQUALS,
////            uuid = "637a2d00bb3d4217b60d1ad155e84d8c")
////    public Message nowStage(User sender, MessageChain messageChain, Contact subject) {
////        Group group = (Group) subject;
////        if (battleLogic.isEnabled(modelName, group.getId())) {
////            if (battleLogic.isCreatedGroup(group.getId())) {
////                /* 如果当前不存在boss（说明是第一次或者被clear掉了，放入一周目boss）*/
////                List<NowBoss> nowBossQuery = battleLogic.getNowBossQuery(group.getId());
////                if (CollUtil.isEmpty(nowBossQuery)) {
////                    List<BattleBoss> bosses = battleLogic.initBoss(group.getId());
////                    if (CollUtil.isEmpty(bosses)) {
////                        return new PlainText("主人你写错代码了吧！快看日志");
////                    } else {
////                        return new PlainText(BattleFormat.INSTANCE.nowBossInfo(1, bosses));
////                    }
////                } else {
////                    // 说明nowBoss表中确实有数据
////                    return new PlainText(BattleFormat.INSTANCE.nowBoss1Info(nowBossQuery.get(0).getBossRounds(), nowBossQuery));
////                }
////            } else {
////                return new PlainText("主人还没有创建工会哦");
////            }
////        } else {
////            // TODO: 2021/6/4 公会战插件应该是默认开启的
////            return new PlainText("请先启动公会战插件~");
////        }
////    }
////
////
////    @Command(value = "创建公会", commandType = CommandType.GROUP, pattern = Pattern.EQUALS,
////            uuid = "a8fe759a62004acead3091d01cb11269")
////    public Message createBattleGroup(User sender, MessageChain messageChain, Contact subject) throws Exception {
////        Group group = (Group) subject;
////        if (battleLogic.createBattleGroup(group)) {
////            // TODO: 2021/6/4 后续改用数据库存储+程序init
////            InputStream nico = new ClassPathResource("emoticon/nico.jpg").getInputStream();
////            CompletableFuture<Image> future = StreamMessageUtil.INSTANCE.generateImageAsync(group, nico);
////            return new PlainText("创建工会成功").plus(future.get());
////        } else {
////            return new PlainText("创建公会失败");
////        }
////    }
////
////    @Command(value = "加入公会", commandType = CommandType.GROUP, pattern = Pattern.EQUALS, alias = {"加入工会"})
////    public Message addBattleUser(User sender, MessageChain messageChain, Contact subject) {
////        Group group = (Group) subject;
////        battleLogic.addBattleUser(sender, group.getId());
////        return new At(sender.getId()).plus(new PlainText("已加入本公会"));
////    }
////
////    @Command(value = "加入全部成员", commandType = CommandType.GROUP, pattern = Pattern.EQUALS)
////    public Message addAllBattleUser(User sender, MessageChain messageChain, Contact subject) {
////        Group group = (Group) subject;
////        battleLogic.addAllBattleUser(group.getMembers(), group.getId());
////        return Image.fromId("{997C294C-1FEE-A4EF-CE1D-7AC055D0BA2A}.jpg").plus(new PlainText("添加群成员成功"));
////    }
////
////    /**
////     * 对公会战boss进行巨大打击
////     */
////    @Command(value = "报刀", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
////    public Message fuckBoss(User sender, MessageChain messageChain, Contact subject) throws Exception {
////        Group group = (Group) subject;
////        return battleLogic.fuckBoss(sender, messageChain, group, true);
////    }
////
////    @Command(value = "尾刀", commandType = CommandType.GROUP, pattern = Pattern.PREFIX)
////    public Message fuckLastBoss(User sender, MessageChain messageChain, Contact subject) throws Exception {
////        Group group = (Group) subject;
////        return battleLogic.fuckBoss(sender, messageChain, group, false);
////    }
////
////    @Command(value = "撤销", commandType = CommandType.GROUP, pattern = Pattern.EQUALS)
////    public Message revertBossDamage(User sender, MessageChain messageChain, Contact subject) throws Exception {
////        Group group = (Group) subject;
////        NormalMember member = (NormalMember) sender;
////        if (member.getPermission().getLevel() >= MemberPermission.ADMINISTRATOR.getLevel()) {
////            String result = battleLogic.revertBossDamage(sender, messageChain, group);
////            return new PlainText(result);
////        } else {
////            return new PlainText("无权撤销");
////        }
////    }
////
////    @Command(value = "预约([1-5])+", commandType = CommandType.GROUP, pattern = Pattern.REGEX)
////    public Message orderBoss(User sender, MessageChain messageChain, Contact subject) throws Exception {
////        Group group = (Group) subject;
////        String result = battleLogic.orderBoss(sender, messageChain, group);
////        if (result == null) return null;
////        return new PlainText(result);
////    }
//
//
//}
//
//
