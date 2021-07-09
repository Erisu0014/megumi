package com.erisu.cloud.megumi.util

object PythonRunner {
    //    final const val pythonPath="/root/yes/envs/megumi/bin/python3"
    private const val pythonPath = "D:\\anconda\\python"
    fun runPythonScript(path: String, args: Array<String>) {
        //todo  此处为本地python目录
        val pyArgs = arrayOf(pythonPath, path, *args)
//            val param:String[]="python",path,param
        val proc: Process = ProcessBuilder(pyArgs.toList()).start() // 执行py文件
//        //用输入输出流来截取结果
//        val reader = BufferedReader(InputStreamReader(proc.inputStream))
//        proc.inputStream.reader().use {
//            println(it.readText())
//        }
//        reader.close()
        proc.waitFor()
    }
}

fun main() {
    val path = System.getProperty("user.dir")
    println("工作目录 = $path")
}