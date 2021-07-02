package com.erisu.cloud.megumi.util

import java.io.BufferedReader
import java.io.InputStreamReader


object PythonRunner {
    fun runPythonScript(path: String, args: Array<String>) {
        val pyArgs = arrayOf("python", path, *args)
//            val param:String[]="python",path,param
        val proc: Process = Runtime.getRuntime().exec(pyArgs) // 执行py文件
        //用输入输出流来截取结果
        val reader = BufferedReader(InputStreamReader(proc.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }
        reader.close()
        proc.waitFor()
    }
}

fun main() {
    val path = System.getProperty("user.dir")
    println("工作目录 = $path")
}