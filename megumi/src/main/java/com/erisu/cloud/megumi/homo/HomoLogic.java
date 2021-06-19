package com.erisu.cloud.megumi.homo;

import cn.hutool.core.io.file.FileReader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigInteger;
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
    private final Map<BigInteger, String> homoMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        try {
            File homo = ResourceUtils.getFile("classpath:homo.data");
            FileReader fileReader = new FileReader(homo);
            List<String> homoData = fileReader.readLines();
            homoData.forEach(h -> {
                String[] split = h.split(":");
                homoMap.put(new BigInteger(split[0]), split[1]);
            });
//            MapUtil.sort(homoMap, (k1, k2) -> k2 - k1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Resource
//    private ScriptUtil scriptUtil;

    public String homo(String numStr) {
        BigInteger num;
        double num_temp = Double.parseDouble(numStr);
        if (Math.floor(num_temp) == num_temp) {
            num = new BigInteger(numStr);
        } else {
            return "爱丽丝没写，但是你可以当成整数算然后除以10的n次方";
        }
        if (num.compareTo(BigInteger.ZERO) < 0) {
            return "负数有什么论证的必要吗？";
        } else {
            return numStr + "=" + toBeHomo(num);
        }
    }

    private String toBeHomo(BigInteger num) {
        if (homoMap.containsKey(num)) {
            return homoMap.get(num);
        }
        BigInteger div = BigInteger.ZERO;
        for (Map.Entry<BigInteger, String> entry : homoMap.entrySet()) {
            if (num.compareTo(entry.getKey()) >= 0) {
                div = entry.getKey();
                break;
            }
        }
        return "" + toBeHomo(div) + "*(" + toBeHomo(num.divide(div)) + ")" + "+(" + toBeHomo(num.mod(div)) + ")";
    }


}
