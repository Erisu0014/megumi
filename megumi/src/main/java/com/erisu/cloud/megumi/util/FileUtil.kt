package com.erisu.cloud.megumi.util

import cn.hutool.core.io.file.FileNameUtil
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.internal.deps.okhttp3.OkHttpClient
import net.mamoe.mirai.internal.deps.okhttp3.Request
import net.mamoe.mirai.message.data.MessageChainBuilder
import java.io.File
import java.nio.file.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * @Description
 * @Author alice
 * @Date 2021/6/18 11:18
 */
object FileUtil {
    val localStaticPath = "${System.getProperty("user.dir")}${File.separator}static"

    val localCachePath = "${System.getProperty("user.dir")}${File.separator}cache"

    /**
     * 下载文件---返回下载后的文件存储路径
     *
     * @param url    文件地址
     * @param folder 存储文件夹
     * @param suffix 后缀名
     * @param name   文件名
     * @return
     */
    @Throws(Exception::class)
    fun downloadHttpUrl(url: String, folder: String, suffix: String?, name: String?): FileResponse? {
        try {
            val client =
                OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            //转化成byte数组
            if (!response.isSuccessful) {
                return FileResponse(response.code, response.body!!.string(), null)
            }
            val bytes = Objects.requireNonNull(response.body)!!.bytes()
            var filename = name ?: url.substringAfterLast("/")
            val folderPath = Paths.get(folder)
            val desk = Files.exists(folderPath)
            if (!desk) Files.createDirectories(folderPath)
            if (suffix != null) {
                filename = FileNameUtil.mainName(filename) + "." + suffix
            }
            val filePath = Paths.get(folder + File.separator + filename)
            val exists = Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)
            if (exists) Files.delete(filePath)
            val path = Files.write(filePath, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
            return FileResponse(response.code, null, path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取任路径下（单层次）的fileName
     * todo 优化为数组返回
     */
    fun getRandomFile(path: String, suffix: String?): String {
        val fileNames: MutableList<String> = mutableListOf()
        val fileTree: FileTreeWalk =
            File((path)).walk()
        if (suffix == null) {
            fileTree.maxDepth(1) //需遍历的目录层次为1，即无须检查子目录
                .filter { it.isFile } //只挑选文件，不处理文件夹
                .forEach { fileNames.add(it.name) }//循环 处理符合条件的文件
        } else {
            fileTree.maxDepth(1) //需遍历的目录层次为1，即无须检查子目录
                .filter { it.isFile } //只挑选文件，不处理文件夹
                .filter { it.extension in listOf(suffix) }//选择扩展名为txt或者mp4的文件
                .forEach { fileNames.add(it.name) }//循环 处理符合条件的文件
        }
        return "$path${File.separator}${fileNames[Random.nextInt(0, fileNames.size)]}"
    }


     suspend fun buildImages(group: Group, pics:MutableList<String>, chainBuilder: MessageChainBuilder){
        pics.forEach {
            val pathResponse = downloadHttpUrl(it, "cache", null, null)
            if (pathResponse != null && pathResponse.code == 200) {
                val image = StreamMessageUtil.generateImage(group, pathResponse.path!!.toFile(), false)
                chainBuilder.append(image)
            }
        }
    }
}