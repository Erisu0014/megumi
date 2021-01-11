package com.erisu.cloud.megumi.jap;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erisu.cloud.megumi.jap.mapper.JapWordsMapper;
import com.erisu.cloud.megumi.jap.pojo.JapWords;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description jap相关
 * @Author alice
 * @Date 2021/1/9 15:59
 **/
@Slf4j
@Component
public class JapUtil {
    @Resource
    private JapWordsMapper japWordsMapper;
    @Value("${isJapRebuild}")
    private Boolean isJapRebuild;


    @PostConstruct
    private void init() throws Exception {
        if (!isJapRebuild) {
            return;
        }
        // init datas
        List<JapWords> japWordsList = new ArrayList<>();
        InputStream is = new FileInputStream(new File("D:\\ideaProjects\\megumiBot\\megumi\\src\\main\\resources\\jp_zhongji.xlsx"));
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)
        for (Sheet sheet : workbook) {
            for (Row r : sheet) {
                JapWords japWords = new JapWords();
                japWords.setWord(r.getCell(0).getStringCellValue());
                japWords.setPseudonym(r.getCell(1).getStringCellValue());
                japWords.setChinese(r.getCell(2).getStringCellValue());
                japWords.setType(r.getCell(4).getStringCellValue());
                japWordsList.add(japWords);

            }
        }
        // 初始化删除之前的data，起到覆盖作用 ----自增id是不覆盖的
        if (japWordsMapper.selectCount(new QueryWrapper<>()) > 0) {
            QueryWrapper<JapWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("insertTag", 0);
            japWordsMapper.delete(queryWrapper);
        }
        japWordsList.forEach(w -> japWordsMapper.insert(w));
        log.info("sql初始化插入完成");
    }
}
