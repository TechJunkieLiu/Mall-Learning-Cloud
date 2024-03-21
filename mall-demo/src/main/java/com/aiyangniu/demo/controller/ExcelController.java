package com.aiyangniu.demo.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.exception.ApiException;
import com.aiyangniu.common.utils.ExcelUtils.easyexcel.ExcelUtilOne;
import com.aiyangniu.common.utils.ExcelUtils.easypoi.ExcelUtilTwo;
import com.aiyangniu.demo.dto.PmsBrandForEasyExcel;
import com.aiyangniu.demo.dto.PmsBrandForEasyPoi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel工具测试类
 *
 * @author lzq
 * @date 2023/11/06
 */
@Slf4j
@Api(value = "ExcelController", tags = "Excel工具测试类")
@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExcelController {

    private final JdbcTemplate jdbcTemplate;

    @ApiOperation("EasyExcel导出")
    @GetMapping(value = "/exportByEasyExcel")
    public CommonResult<String> exportByEasyExcel(HttpServletResponse response){
        List<PmsBrandForEasyExcel> list = jdbcTemplate.query("SELECT * FROM pms_brand", new Object[]{}, new BeanPropertyRowMapper<>(PmsBrandForEasyExcel.class));
        String fileName = "数据导出" + System.currentTimeMillis();
        try {
            ExcelUtilOne.export(fileName, list, PmsBrandForEasyExcel.class, response);
        } catch (Exception e) {
            return CommonResult.failed("数据导出失败" + e.getMessage());
        }
        return CommonResult.success("数据导出成功！");
    }

    @ApiOperation("EasyExcel导入")
    @PostMapping(value = "/importByEasyExcel")
    public CommonResult<String> importByEasyExcel(@RequestParam("file") MultipartFile file){
        try {
            List<?> list = ExcelUtilOne.importExcel(file, PmsBrandForEasyExcel.class);
            return CommonResult.success("数据导入完成");
        } catch (Exception e) {
            return CommonResult.failed("数据导入失败！" + e.getMessage());
        }
    }

    @ApiOperation("EasyPOI导出")
    @GetMapping(value = "/exportByEasyPOI")
    public void exportByEasyPOI(HttpServletResponse response){
        List<PmsBrandForEasyPoi> list = jdbcTemplate.query("SELECT * FROM pms_brand", new Object[]{}, new BeanPropertyRowMapper<>(PmsBrandForEasyPoi.class));
        String fileName = "数据导出" + System.currentTimeMillis();
        try {
            ExcelUtilTwo.exportExcel(list, PmsBrandForEasyPoi.class, fileName, new ExportParams(), response);
            ExcelUtilTwo.exportExcel(list, "title", "sheetName", PmsBrandForEasyPoi.class, fileName, response);
            ExcelUtilTwo.exportExcel(list, "title", "sheetName", PmsBrandForEasyPoi.class, fileName, false, response);
            ExcelUtilTwo.exportExcel(new ArrayList<>(), fileName, response);
//            ExcelUtilTwo.exportExcel(new TemplateExportParams(), new HashMap<>(16), fileName, response);
//            ExcelUtilTwo.WordTemplateExport(new HashMap<>(16), "D:\\excel\\pms_brand.xlsx", fileName, response);
//            ExcelUtilTwo.WordTemplateExportMorePage(new HashMap<>(16), "D:\\excel\\pms_brand.xlsx", fileName, response);
        } catch (Exception e) {
            throw new ApiException("数据导出失败", e);
        }
    }

    @ApiOperation("EasyPOI导入")
    @PostMapping(value = "/importByEasyPOI")
    public CommonResult<String> importByEasyPOI(@RequestParam("file") MultipartFile file){
        try {
            List<?> list = ExcelUtilTwo.importExcel(file, 1, 1, PmsBrandForEasyPoi.class);
            ExcelImportResult<PmsBrandForEasyPoi> result = ExcelUtilTwo.importExcelMore(file, 1, 1, PmsBrandForEasyPoi.class);
            return CommonResult.success("数据导入完成");
        } catch (Exception e) {
            return CommonResult.failed("数据导入失败！" + e.getMessage());
        }
    }

    @ApiOperation("POI导出")
    @GetMapping(value = "/exportByPOI")
    public void exportByPOI(HttpServletResponse response){




    }
}
