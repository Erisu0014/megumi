package com.erisu.cloud.megumi.battle.util;

import cn.hutool.core.util.RandomUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description 火星文
 * @Author alice
 * @Date 2021/6/10 16:38
 **/
@Slf4j
@Component
public class MarsUtil {
    public String getMars(String text) throws Exception {
        WebClient webclient = new WebClient();
        // 这里是配置一下不加载css和javaScript
        webclient.getOptions().setCssEnabled(false);
        webclient.getOptions().setJavaScriptEnabled(false);
        HtmlPage marsPage = webclient.getPage("http://www.fzlft.com/huo/?");
        final HtmlForm form = marsPage.getForms().get(0);
        final HtmlSubmitInput button = form.getInputByValue("开始生成");
        HtmlTextArea inputArea = form.getTextAreaByName("q");
        inputArea.setText(text);
        final HtmlPage nextPage = button.click();
        final HtmlForm form2 = nextPage.getForms().get(0);
        HtmlTextArea resultArea = form2.getTextAreaByName("");

        String[] result = resultArea.getText().split("\n------------------------------------\n", 5);
        if (result.length == 5) {
            return result[RandomUtil.randomInt(0, 3)].replace("\n", "");
        } else {
            return "这个地球真是一秒钟都待不下去了，886";
        }
    }
}


