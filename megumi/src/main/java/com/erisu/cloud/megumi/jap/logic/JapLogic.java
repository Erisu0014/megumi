package com.erisu.cloud.megumi.jap.logic;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erisu.cloud.megumi.jap.mapper.JapWordsMapper;
import com.erisu.cloud.megumi.jap.pojo.JapWords;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description jap信息发送
 * @Author alice
 * @Date 2021/1/11 10:55
 **/
@Component
public class JapLogic {
    @Resource
    private JapWordsMapper japWordsMapper;

    public PlainText TodayMessageBuilder() {
        Integer count = japWordsMapper.selectCount(new QueryWrapper<>());
        if (count <= 0) {
            return new PlainText("数据库里没录入数据的话，就学习不了了哦");
        }
        List<JapWords> words = japWordsMapper.selectRandom(10);
        StringBuilder sb = new StringBuilder();
        sb.append("今日的单词表~kira⭐\n");
        words.forEach(w ->
                sb.append(w.getWord()).append("(").append(w.getPseudonym()).append(")")
                        .append("\t").append(w.getChinese()).append("\n"));
        return new PlainText(sb.toString());
    }
}
