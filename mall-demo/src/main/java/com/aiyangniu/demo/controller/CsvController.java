package com.aiyangniu.demo.controller;

import com.aiyangniu.common.utils.CsvUtil;
import com.aiyangniu.demo.dto.HuifuTransOrdLog1;
import com.aiyangniu.demo.dto.HuifuTransOrdLog2;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 解析CSV文件测试类
 *
 * @author lzq
 * @date 2024/02/28
 */
@Slf4j
@Api(value = "TestController", tags = "解析CSV文件测试类")
@RestController
@RequestMapping("/csv")
public class CsvController {

    @ApiOperation(value = "解析CSV文件")
    @PostMapping("/test")
    public void test(@RequestPart("file") MultipartFile file) {

        List<CsvUtil.HuifuTransOrdLog> list1 = CsvUtil.getCsvDataMethod1(file);
        List<String[]> list2 = CsvUtil.getCsvDataMethod2(file);
        List<String[]> list3 = CsvUtil.getCsvDataMethod3(file);
        List<HuifuTransOrdLog2> list4 = CsvUtil.getCsvDataMethod4(file, HuifuTransOrdLog2.class);
        List<CsvUtil.HuifuTransOrdLog> list5 = CsvUtil.csvToBeanByPosition(file, CsvUtil.HuifuTransOrdLog.class);
        List<HuifuTransOrdLog1> list6 = CsvUtil.csvToBeanByName(file, HuifuTransOrdLog1.class);
        List<HuifuTransOrdLog1> list7 = CsvUtil.csvToBeanByPositionAnnotation(file, HuifuTransOrdLog1.class);
        List<HuifuTransOrdLog2> list8 = CsvUtil.csvToBeanByNameAnnotation(file, HuifuTransOrdLog2.class);

        System.out.println(JSON.toJSONString(list1));
        System.out.println(JSON.toJSONString(list2));
        System.out.println(JSON.toJSONString(list3));
        System.out.println(JSON.toJSONString(list4));
        System.out.println(JSON.toJSONString(list5));
        System.out.println(JSON.toJSONString(list6));
        System.out.println(JSON.toJSONString(list7));
        System.out.println(JSON.toJSONString(list8));
    }
}
