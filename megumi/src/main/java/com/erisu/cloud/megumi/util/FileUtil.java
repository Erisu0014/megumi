package com.erisu.cloud.megumi.util;

import cn.hutool.core.io.file.FileNameUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
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
     * @param folder 存储文件夹
     * @param suffix 后缀名
     * @param name   文件名
     * @return
     */
    public static Path downloadHttpUrl(String url, String folder, @Nullable String suffix, @Nullable String name) throws Exception {
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
            String filename = name == null ? StringUtils.substringAfterLast(url, "/") : name;
            Path folderPath = Paths.get(folder);
            boolean desk = Files.exists(folderPath);
            if (!desk) {
                Files.createDirectories(folderPath);
            }
            if (suffix != null) {
                filename = FileNameUtil.mainName(filename) + "." + suffix;
            }
            Path filePath = Paths.get(folder + File.separator + filename);
            boolean exists = Files.exists(filePath, LinkOption.NOFOLLOW_LINKS);
            if (exists) {
                Files.delete(filePath);
            }
            return Files.write(filePath, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
