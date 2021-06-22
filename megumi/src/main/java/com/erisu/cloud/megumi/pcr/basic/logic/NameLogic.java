package com.erisu.cloud.megumi.pcr.basic.logic;

import com.erisu.cloud.megumi.pcr.basic.mapper.PcrAvatarMapper;
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrAvatar;
import com.erisu.cloud.megumi.pcr.basic.pojo.PcrPrincess;
import com.erisu.cloud.megumi.util.FileUtil;
import com.erisu.cloud.megumi.util.MessageUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description NAME相关
 * @Author alice
 * @Date 2021/6/22 9:47
 **/
@Component
public class NameLogic {
    @Resource
    private PcrAvatarMapper avatarMapper;

    public Message getAvatar(User sender, Group group, String princessId) throws Exception {
        PcrAvatar pcrAvatar = avatarMapper.searchMaxStarAvatar(princessId);
        Path imagePath = FileUtil.downloadHttpUrl(pcrAvatar.getAvatarUrl(), "image", null);
        if (imagePath != null) {
            List<String> nameList = PcrInitData.INSTANCE.getIdMap().get(pcrAvatar.getPrincessId());
            CompletableFuture<Image> future = MessageUtil.INSTANCE.generateImageAsync(group, imagePath.toFile(), true);
            return MessageUtil.INSTANCE.message(new At(sender.getId()),
                    future.get(), new PlainText(nameList.get(0)));
        } else {
            return null;
        }

    }
}
