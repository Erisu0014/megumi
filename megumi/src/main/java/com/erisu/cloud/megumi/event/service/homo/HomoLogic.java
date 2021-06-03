package com.erisu.cloud.megumi.event.service.homo;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 不要相信homo
 * @Author alice
 * @Date 2021/1/5 17:45
 **/
@Component
public class HomoLogic {
    // 保证有序
    private final Map<Integer, String> homoMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        try {
            File homo = ResourceUtils.getFile("classpath:homo.data");
            FileReader fileReader = new FileReader(homo);
            List<String> homoData = fileReader.readLines();
            homoData.forEach(h -> {
                String[] split = h.split(":");
                homoMap.put(Integer.valueOf(split[0]), split[1]);
            });
//            MapUtil.sort(homoMap, (k1, k2) -> k2 - k1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Resource
//    private ScriptUtil scriptUtil;

    public String homo(String numStr) {
        // 啥bNashorn不懂es6
//        Object homo = scriptUtil.executeJs( "D:\\ideaProjects\\megumiBot\\megumi\\src\\main\\resources\\homo.js", "homo", 123);
        Integer num = Integer.valueOf(numStr);
        if (num < 0) {
            return "负数有什么论证的必要吗？";
        } else {
            return numStr + "=" + toBeHomo(num);
        }
    }

    private String toBeHomo(Integer num) {
        if (homoMap.containsKey(num)) {
            return homoMap.get(num);
        }
        Integer div = 0;
        for (Map.Entry<Integer, String> entry : homoMap.entrySet()) {
            if (num >= entry.getKey()) {
                div = entry.getKey();
                break;
            }
        }
        return "" + toBeHomo(div) + "*(" + toBeHomo(num / div) + ")" + "+(" + toBeHomo(num % div) + ")";
    }


}
