package com.erisu.cloud.megumi.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.*;
import java.util.Objects;

/**
 * @Description
 * @Author alice
 * @Date 2021/6/18 11:18
 **/
public class FileUtil {

    /**
     * 下载文件---返回下载后的文件存储路径
     *
     * @param url    文件地址
     * @param folder 存储文件名
     * @return
     */
    public static Path downloadHttpUrl(String url, String folder) throws Exception {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //访问路径
                    .url(url)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            //转化成byte数组
            byte[] bytes = Objects.requireNonNull(response.body()).bytes();
            //切割出图片名称==》PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png
            String filename = StringUtils.substringAfterLast(url, "/");
            Path folderPath = Paths.get(folder);
            boolean desk = Files.exists(folderPath);
            if (!desk) {
                Files.createDirectories(folderPath);
            }
            Path filePath = Paths.get(folder + File.separator + filename);
//            boolean exists = Files.exists(filePath, LinkOption.NOFOLLOW_LINKS);
//            非重新下载方式
            return Files.write(filePath, bytes, StandardOpenOption.CREATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
