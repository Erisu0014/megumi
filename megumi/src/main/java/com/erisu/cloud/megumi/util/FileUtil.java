package com.erisu.cloud.megumi.util;

import cn.hutool.core.io.file.FileNameUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
     * @param suffix 后缀名
     * @return
     */
    public static Path downloadHttpUrl(String url, String folder, String suffix) throws Exception {
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
            String filename = StringUtils.substringAfterLast(url, "/");
            Path folderPath = Paths.get(folder);
            boolean desk = Files.exists(folderPath);
            if (!desk) {
                Files.createDirectories(folderPath);
            }
            if (suffix != null) {
                filename = FileNameUtil.mainName(filename) + "." + suffix;
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
