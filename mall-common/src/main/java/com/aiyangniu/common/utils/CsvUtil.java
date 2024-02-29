package com.aiyangniu.common.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenCSV解析CSV文件工具类
 *
 * @author lzq
 * @date 2024/02/28
 */
public class CsvUtil {

    private static final Logger logger = LogManager.getLogger(CsvUtil.class);

    /**
     * 解析csv文件并转成bean（方法一）
     */
    public static List<HuifuTransOrdLog> getCsvDataMethod1(MultipartFile file) {
        List<HuifuTransOrdLog> transOrdLogList = new ArrayList<>();
        InputStreamReader in;
        try {
            in = new InputStreamReader(file.getInputStream(), "GBK");
            BufferedReader bufferedReader = new BufferedReader(in);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                HuifuTransOrdLog huifuTransOrdLog = new HuifuTransOrdLog();
                huifuTransOrdLog.setSysId(splitResult(split[0]));
                huifuTransOrdLog.setHuifuId(splitResult(split[1]));
                huifuTransOrdLog.setRegName(splitResult(split[2]));
                transOrdLogList.add(huifuTransOrdLog);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transOrdLogList;
    }

    private static String splitResult(String once) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < once.length(); i++) {
            if (once.charAt(i) != '"') {
                result.append(once.charAt(i));
            }
        }
        return result.toString();
    }

    @Data
    public static class HuifuTransOrdLog {
        @ApiModelProperty("渠道商号")
        private String sysId;
        @ApiModelProperty("商户号")
        private String huifuId;
        @ApiModelProperty("商户名称")
        private String regName;
    }

    /**
     * 解析csv文件并转成bean（方法二）
     */
    public static List<String[]> getCsvDataMethod2(MultipartFile file) {
        List<String[]> list = new ArrayList<>();
        int i = 0;
        try {
            CSVReader csvReader = new CSVReaderBuilder(
                    new BufferedReader(
                            new InputStreamReader(file.getInputStream(), "GBK"))).build();
            for (String[] next : csvReader) {
                // 去除第一行的表头，从第二行开始
                if (i >= 1) {
                    list.add(next);
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("CSV文件读取异常！");
        }
        return list;
    }

    /**
     * 解析csv文件并转成bean（方法三）
     */
    public static List<String[]> getCsvDataMethod3(MultipartFile file) {
        InputStreamReader reader;
        List<String[]> list = new ArrayList<>();
        try {
            reader = new InputStreamReader(file.getInputStream(), "GBK");
            CSVReader csvReader = new CSVReaderBuilder(reader).build();
            list = csvReader.readAll();
            reader.close();
        } catch (Exception e) {
            logger.error("CSV文件读取异常！");
        }
        return list;
    }

    /**
     * 解析csv文件并转成bean（方法四）
     */
    public static <T> List<T> getCsvDataMethod4(MultipartFile file, Class<T> clazz) {
        InputStreamReader reader;
        CsvToBean<T> csvToBean;
        List<T> list = new ArrayList<>();
        try {
            reader = new InputStreamReader(file.getInputStream(), "GBK");
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(reader).withSeparator(',').withQuoteChar('\'').withMappingStrategy(strategy).build();
            list = csvToBean.parse();
            reader.close();
        } catch (Exception e) {
            logger.error("数据转化失败！");
        }
        return list;
    }

    /**
     * 基于位置映射的读取
     */
    public static <T> List<T> csvToBeanByPosition(MultipartFile file, Class<T> clazz) {
        InputStreamReader reader;
        CsvToBean<T> csvToBean;
        List<T> list = new ArrayList<>();
        try {
            reader = new InputStreamReader(file.getInputStream(), "GBK");
            // 不需要标题行，列的顺序通过列位置映射指定
            ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<>();
            String[] columns = new String[] {"sysId", "huifuId", "regName"};
            strategy.setColumnMapping(columns);
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(reader).withSeparator(',').withQuoteChar('\'').withMappingStrategy(strategy).build();
            list = csvToBean.parse();
            reader.close();
        } catch (Exception e) {
            logger.error("数据转化失败！");
        }
        return list;
    }

    /**
     * 基于列名映射的读取
     */
    public static <T> List<T> csvToBeanByName(MultipartFile file, Class<T> clazz) {
        InputStreamReader reader;
        CsvToBean<T> csvToBean;
        List<T> list = new ArrayList<>();
        try {
            reader = new InputStreamReader(file.getInputStream(), "GBK");
            // bean的字段名称大写为标题列名
            csvToBean = new CsvToBeanBuilder<T>(reader).withSeparator(',').withQuoteChar('\'').withType(clazz).build();
            list = csvToBean.parse();
            reader.close();
        } catch (Exception e) {
            logger.error("数据转化失败！");
        }
        return list;
    }

    /**
     * 基于@CsvBindByPosition注解映射的读取
     */
    public static <T> List<T> csvToBeanByPositionAnnotation(MultipartFile file, Class<T> clazz) {
        InputStreamReader reader;
        CsvToBean<T> csvToBean;
        List<T> list = new ArrayList<>();
        try {
            reader = new InputStreamReader(file.getInputStream(), "GBK");
            // 不需要标题行，列的顺序通过CsvBindByPosition注解的position属性指定
            csvToBean = new CsvToBeanBuilder<T>(reader).withSeparator(',').withQuoteChar('\'').withType(clazz).build();
            list = csvToBean.parse();
            reader.close();
        } catch (Exception e) {
            logger.error("数据转化失败！");
        }
        return list;
    }

    /**
     * 基于@CsvBindByName注解映射的读取
     */
    public static <T> List<T> csvToBeanByNameAnnotation(MultipartFile file, Class<T> clazz) {
        InputStreamReader reader;
        CsvToBean<T> csvToBean;
        List<T> list = new ArrayList<>();
        try {
            reader = new InputStreamReader(file.getInputStream(), "GBK");
            // CsvBindByName注解的column属性为标题列名
            csvToBean = new CsvToBeanBuilder<T>(reader).withSeparator(',').withQuoteChar('\'').withType(clazz).build();
            list = csvToBean.parse();
            reader.close();
        } catch (Exception e) {
            logger.error("数据转化失败！");
        }
        return list;
    }
}
