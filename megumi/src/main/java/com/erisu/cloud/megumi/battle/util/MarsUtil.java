package com.erisu.cloud.megumi.battle.util;

import cn.hutool.core.util.RandomUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharSet;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 火星文
 * @Author alice
 * @Date 2021/6/10 16:38
 **/
@Slf4j
@Component
public class MarsUtil {
    public String getMars(String text) throws Exception {
        WebClient webclient = new WebClient(BrowserVersion.CHROME);
        // 这里是配置一下不加载css和javaScript
        webclient.getOptions().setCssEnabled(false);
        webclient.getOptions().setJavaScriptEnabled(false);
        WebRequest webRequest = new WebRequest(new URL("http://www.fzlft.com/huo/?"), HttpMethod.POST);
        List<NameValuePair> list = new ArrayList<>();
        list.add(new NameValuePair("t", ""));
        list.add(new NameValuePair("q", text));
        webRequest.setRequestParameters(list);
        webRequest.setCharset(StandardCharsets.UTF_8);
        webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9,und;q=0.8,en;q=0.7");
        HtmlPage marsPage = webclient.getPage(webRequest);
        final HtmlForm form2 = marsPage.getForms().get(0);
        HtmlTextArea resultArea = null;
        try {
            resultArea = form2.getTextAreaByName("");
        } catch (Exception e) {
            return "ご前喕白勺吙☆，鉯鮜záＩ婡τáη索ò巴～ご";
        }

        String[] result = resultArea.getText().split("\n------------------------------------\n", 5);
        if (result.length == 5) {
            return result[RandomUtil.randomInt(0, 3)].replace("\n", "");
        } else {
            return "〗這個地球塡惿壹眇鐘都鴏芣下詓了，八八六〖";
        }
    }
}


