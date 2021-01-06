package com.erisu.cloud.megumi.util;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Description 运行其他语言脚本
 * @Author alice
 * @Date 2021/1/5 18:05
 **/
@Component
public class ScriptUtil {
    /**
     * sha b es6
     * @param path         文件路径
     * @param functionName 函数名
     * @param args         参数列表
     * @return
     * @throws Exception
     */
    public Object executeJs(String path, String functionName, Object... args) throws Exception {
        String[] options = new String[]{"--language=es6"};
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        NashornScriptEngine engine = (NashornScriptEngine) factory.getScriptEngine(options);
        engine.eval(Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8));
        return engine.invokeFunction(functionName, args);
    }
}
