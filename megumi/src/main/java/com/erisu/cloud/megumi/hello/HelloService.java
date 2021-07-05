package com.erisu.cloud.megumi.hello;

import cn.hutool.core.util.StrUtil;
import com.erisu.cloud.megumi.battle.util.MarsUtil;
import com.erisu.cloud.megumi.command.Command;
import com.erisu.cloud.megumi.command.CommandType;
import com.erisu.cloud.megumi.pattern.Pattern;
import com.erisu.cloud.megumi.plugin.pojo.Model;
import com.erisu.cloud.megumi.util.FileUtil;
import com.erisu.cloud.megumi.util.PatternUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.file.Path;

/**
 * @Description hello world girl
 * @Author alice
 * @Date 2020/12/10 16:25
 **/
@Slf4j
@Component
@Model(name = "hello")
public class HelloService {
    @Resource
    private MarsUtil marsUtil;

    @Command(commandType = CommandType.GROUP, value = "在吗", pattern = Pattern.CONTAINS, alias = {"zaima", "zai"})
    public Message hello(User sender, MessageChain messageChain, Contact subject) throws Exception {
        return new PlainText("はい！私はいつも貴方の側にいるよ～");
    }

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS, uuid = "6cbd28fa91f0469698dff603d8635fca")
    public Message showImage(User sender, MessageChain messageChain, Contact subject) throws Exception {
        String content = ((PlainText) messageChain.get(1)).getContent().trim();
        String imageUrl = PatternUtil.INSTANCE.checkRemoteImage(content);
        if (imageUrl == null) return null;
        Path path = FileUtil.downloadHttpUrl(imageUrl, "image", null, null);
        if (path != null) {
            ExternalResource externalResource = ExternalResource.create(path.toFile());
            return subject.uploadImage(externalResource);
        } else {
            return null;
        }
    }

    @Command(commandType = CommandType.GROUP, pattern = Pattern.CONTAINS, uuid = "703065f9c4534286b21f7e8b22b1f9f6")
    public Message showAudio(User sender, MessageChain messageChain, Contact subject) throws Exception {
        Group g = (Group) subject;
        String content = ((PlainText) messageChain.get(1)).getContent().trim();
        String audioUrl = PatternUtil.INSTANCE.checkRemoteAudio(content);
        if (audioUrl == null) return null;
        Path path = FileUtil.downloadHttpUrl(audioUrl, "audio", null, null);
        if (path != null) {
            ExternalResource externalResource = ExternalResource.create(path.toFile());
            return ExternalResource.uploadAsVoice(externalResource, subject);
        } else {
            return null;
        }
    }

    @Command(commandType = CommandType.GROUP, value = "火星文", pattern = Pattern.PREFIX)
    public Message mars(User sender, MessageChain messageChain, Contact subject) throws Exception {
        String content = ((PlainText) messageChain.get(1)).getContent();
        String marsText = StrUtil.removePrefix(content, "火星文").trim();
        return new PlainText(marsUtil.getMars(marsText));
    }


}
