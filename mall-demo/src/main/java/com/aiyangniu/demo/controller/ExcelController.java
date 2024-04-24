package com.aiyangniu.demo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.exception.ApiException;
import com.aiyangniu.common.utils.ExcelUtils.easyexcel.ExcelUtilOne;
import com.aiyangniu.common.utils.ExcelUtils.easypoi.ExcelExportVo;
import com.aiyangniu.common.utils.ExcelUtils.easypoi.ExcelStyleUtil;
import com.aiyangniu.common.utils.ExcelUtils.easypoi.ExcelUtilTwo;
import com.aiyangniu.demo.dto.PmsBrandForEasyExcel;
import com.aiyangniu.demo.dto.PmsBrandForEasyPoi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    @ApiOperation("EasyPoi导出")
    @GetMapping(value = "/exportByEasyPoi")
    public void exportByEasyPoi(HttpServletResponse response){
        List<PmsBrandForEasyPoi> list = jdbcTemplate.query("SELECT * FROM pms_brand", new Object[]{}, new BeanPropertyRowMapper<>(PmsBrandForEasyPoi.class));
        String fileName = "数据导出" + System.currentTimeMillis();
        try {
            ExcelUtilTwo.exportExcel(new ExportParams(), list, PmsBrandForEasyPoi.class, fileName, Boolean.TRUE, response);
            ExcelUtilTwo.exportExcel(list, PmsBrandForEasyPoi.class, fileName, "sheetName", "title", response);
            ExcelUtilTwo.exportExcel(list, "title", "sheetName", PmsBrandForEasyPoi.class, fileName, false, response);
            ExcelUtilTwo.exportExcel(list, PmsBrandForEasyPoi.class, fileName, response);
            ExcelUtilTwo.exportExcel(new ArrayList<>(), fileName, response);
            ExcelUtilTwo.exportExcel(new TemplateExportParams(), new HashMap<>(16), fileName, response);
            ExcelUtilTwo.WordTemplateExport(new HashMap<>(16), "D:\\excel\\pms_brand.xlsx", fileName, response);
            ExcelUtilTwo.WordTemplateExportMorePage(new HashMap<>(16), "D:\\excel\\pms_brand.xlsx", fileName, response);
        } catch (Exception e) {
            throw new ApiException("数据导出失败", e);
        }
    }

    @ApiOperation("EasyPoi导出（多级表头和多个sheet）")
    @GetMapping(value = "/exportByEasyPoiPlus")
    public void exportByEasyPoiPlus(HttpServletResponse response){
        // 模拟数据
        List<ExcelExportVo> dataList = new ArrayList<>();
        List<ExcelExportVo.UserInfo> userInfoList = new ArrayList<>();
        List<ExcelExportVo.RoleInfo> roleInfoList = new ArrayList<>();
        userInfoList.add(new ExcelExportVo.UserInfo("chenyi","陈仪","12345678910","战神联盟","科研部"));
        userInfoList.add(new ExcelExportVo.UserInfo("honger","洪洱","13241220000","前途有限公司","市场部"));
        userInfoList.add(new ExcelExportVo.UserInfo("zhangsan","张三","12544445555","前途有限公司","研发部"));
        userInfoList.add(new ExcelExportVo.UserInfo("lisi","李四","13125223561","战神联盟","科研部"));
        userInfoList.add(new ExcelExportVo.UserInfo("wangwu","王五","15423226355","战神联盟","销售部"));

        roleInfoList.add(new ExcelExportVo.RoleInfo("经理","JL","sys,dept,role,menu","view,add,update,delete"));
        roleInfoList.add(new ExcelExportVo.RoleInfo("经理","JL","sys,dept,role,menu","view,add,update,delete"));
        roleInfoList.add(new ExcelExportVo.RoleInfo("组长","ZZ","role,menu","view,add,update"));
        roleInfoList.add(new ExcelExportVo.RoleInfo("普通","PT","menu","view,add"));
        roleInfoList.add(new ExcelExportVo.RoleInfo("普通","PT","menu","view,add"));

        ExcelExportVo vo = new ExcelExportVo();
        vo.setUserInfoList(userInfoList);
        vo.setRoleInfoList(roleInfoList);
        dataList.add(vo);

        // 第一个sheet
        ExportParams params1 = new ExportParams();
        params1.setStyle(ExcelStyleUtil.class);
        params1.setSheetName("用户信息1");
        params1.setTitle("用户信息");
        Map<String, Object> params1Map = new HashMap<>();
        params1Map.put("title", params1);
        params1Map.put("entity", ExcelExportVo.class);
        params1Map.put("data", dataList);

        // 第二个sheet
        ExportParams params2 = new ExportParams();
        params2.setStyle(ExcelStyleUtil.class);
        params2.setSheetName("用户信息2");
        Map<String, Object> params2Map = new HashMap<>();
        params2Map.put("title", params2);
        params2Map.put("entity", ExcelExportVo.class);
        params2Map.put("data", new ArrayList<>());

        // 第三个sheet
        ExportParams params3 = new ExportParams();
        params3.setStyle(ExcelStyleUtil.class);
        params3.setSheetName("用户信息3");
        Map<String, Object> params3Map = new HashMap<>();
        params3Map.put("title", params3);
        params3Map.put("entity", ExcelExportVo.class);
        params3Map.put("data", new ArrayList<>());

        // 将3个sheet封装
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(params1Map);
        sheetsList.add(params2Map);
        sheetsList.add(params3Map);
        Workbook workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
        ExcelUtilTwo.downLoadExcel("用户信息", response, workBook);
    }

    @ApiOperation("EasyPoi导入")
    @PostMapping(value = "/importByEasyPOI")
    public CommonResult<String> importByEasyPOI(@RequestParam("file") MultipartFile file){
        try {
            List<PmsBrandForEasyPoi> list1 = ExcelUtilTwo.importExcel("D:\\excel\\pms_brand.xlsx", 0, 1, PmsBrandForEasyPoi.class);
            List<PmsBrandForEasyPoi> list2 = ExcelUtilTwo.importExcel(file, 0, 1, false, PmsBrandForEasyPoi.class);
            List<PmsBrandForEasyPoi> list3 = ExcelUtilTwo.importExcel(file, "sheetName", 0, 1, 1, 0, PmsBrandForEasyPoi.class);
            return CommonResult.success("数据导入完成");
        } catch (Exception e) {
            return CommonResult.failed("数据导入失败！" + e.getMessage());
        }
    }


    @ApiOperation("Poi导出")
    @GetMapping(value = "/exportByPoi")
    public void exportByPoi(HttpServletResponse response){

    }
}
