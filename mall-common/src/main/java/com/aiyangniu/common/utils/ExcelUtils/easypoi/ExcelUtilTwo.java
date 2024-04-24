package com.aiyangniu.common.utils.ExcelUtils.easypoi;

import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.parse.ParseWord07;
import com.aiyangniu.common.exception.Asserts;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * EasyPoi 工具类
 * 包括多级表头、多个sheet导出
 *
 * @author lzq
 * @date 2024/04/02
 */
public class ExcelUtilTwo {

    /**
     * 导出
     *
     * @param params 导出参数(title表格内数据标题、sheet名称、是否创建表头、表格类型)
     * @param list 数据
     * @param pojoClass pojo类型
     * @param fileName 文件名称
     * @param isSetRowBackground 是否设置隔行背景色
     */
    public static void exportExcel(ExportParams params, List<?> list, Class<?> pojoClass, String fileName, boolean isSetRowBackground, HttpServletResponse response){
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(params, pojoClass ,list);
        if (ObjectUtils.isEmpty(workbook)){
            Asserts.fail("工作簿不能为空！");
        }
        if (isSetRowBackground){
            // 偶数行设置背景色
            setRowBackground(workbook);
        }
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 导出
     *
     * @param list 数据
     * @param pojoClass pojo类型
     * @param fileName 文件名称
     * @param sheetName sheet名称
     * @param title 标题
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, String sheetName, String title, HttpServletResponse response){
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setStyle(ExcelStyleUtil.class);
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 导出
     *
     * @param list 数据
     * @param title 标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName 文件名称
     * @param isCreateHeader 是否创建表头
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response){
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 导出
     *
     * @param list 数据
     * @param pojoClass pojo类型
     * @param fileName 文件名称
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response){
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(ExcelStyleUtil.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 导出
     *
     * @param list 数据
     * @param fileName 文件名称
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response){
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(ExcelStyleUtil.class);
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.XSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 根据模板批量导出
     *
     * @param templatePath 模板路径
     * @param map 数据集合
     * @param fileName 文件名
     */
    public static void exportExcel(TemplateExportParams templatePath, Map<String, Object> map, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(templatePath, map);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * word模板批量导出
     */
    public static void WordTemplateExport(Map<String, Object> map, String templatePath, String fileName, HttpServletResponse response) throws Exception {
        XWPFDocument doc = WordExportUtil.exportWord07(templatePath, map);
        downLoadWord(fileName, response, doc);
    }

    /**
     * word模板批量导出多页
     */
    public static void WordTemplateExportMorePage(Map<String, Object> map, String templatePath, String fileName, HttpServletResponse response) throws Exception {
        XWPFDocument doc = new ParseWord07().parseWord(templatePath, map);
        downLoadWord(fileName, response, doc);
    }

    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downLoadWord(String fileName, HttpServletResponse response, XWPFDocument doc){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/msword");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".docx", "UTF-8"));
            doc.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setRowBackground(Workbook workbook){
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle styles = ExcelStyleUtil.getStyles(workbook, false, (short) 12);
        for(int i = 0; i <= sheet.getLastRowNum(); i ++) {
            Row row = sheet.getRow(i);
            if (i%2==0){
                for(int j = 0; j < row.getPhysicalNumberOfCells(); j ++) {
                    Cell cell = row.getCell(j);
                    cell.setCellStyle(styles);
                }
            }
        }
    }

    /**
     * 导入
     *
     * @param filePath excel文件路径
     * @param titleRows 标题行
     * @param headerRows 表头行
     * @param pojoClass pojo类型
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(true);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空！");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 导入
     *
     * @param file 上传的文件
     * @param titleRows 标题行
     * @param headerRows 表头行
     * @param needVerify 是否检验excel内容（有错误信息）
     * @param pojoClass pojo类型
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, boolean needVerify, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            InputStream inputStream = file.getInputStream();
            ImportParams params = new ImportParams();
            params.setTitleRows(titleRows);
            params.setHeadRows(headerRows);
            params.setSaveUrl("upload/excel/");
            params.setNeedSave(true);
            params.setNeedVerify(needVerify);
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 读取指定sheet的数据
     *
     * @param file 上传的文件
     * @param sheetName 要读取的sheetName
     * @param titleRows 表头行数
     * @param headRows 标题行数
     * @param startRows 表头之前有多少行不要的数据，从1开始，忽略空行
     * @param readRows 要读取多少行数据，从0开始，比如读取10行，值就是9; 不指定时默认为0
     * @param pojoClass 实体
     */
    public static <T> List<T> importExcel(MultipartFile file, String sheetName, Integer titleRows, Integer headRows, Integer startRows, Integer readRows, Class<T> pojoClass) throws Exception {
        // 上传文件，返回一个workbook
        Workbook workbook = importExcel(file);
        int numberOfSheets = workbook.getNumberOfSheets();
        List<T> list = null;
        for (int i = 0; i < numberOfSheets; i++) {
            String name = workbook.getSheetName(i).trim();
            if (name.equals(sheetName) || name.endsWith(sheetName)){
                ImportParams params = new ImportParams();
                params.setTitleRows(titleRows);
                params.setHeadRows(headRows);
                params.setStartRows(startRows);
                params.setReadRows(readRows);
                // 第几个sheet页
                params.setStartSheetIndex(i);
                final ExcelImportService excelImportService = new ExcelImportService();
                ExcelImportResult<T> result = excelImportService.importExcelByIs(file.getInputStream(), pojoClass, params, false);
                list = result.getList();
                break;
            }
        }
        return list;
    }

    public static Workbook importExcel(MultipartFile file) throws IOException {
        File toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        Workbook workbook = null;
        if(toFile.getPath().endsWith("xls")){
            workbook = new HSSFWorkbook(file.getInputStream());
        }else if(toFile.getPath().endsWith("xlsx")){
            workbook = new XSSFWorkbook(file.getInputStream());
        }else {
            throw new RuntimeException("请确认你上传的文件类型");
        }
        return workbook;
    }

    /**
     * excel转html预览
     */
    public static void excelToHtml(String filePath, HttpServletResponse response) throws Exception {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile(filePath)), true);
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }

    /**
     * 以map的形式导出表格
     *
     * @param list 数据
     */
    public static <T> List<Map<String, Object>> objectToMap(List<T> list){
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map = null;
        try {
            for (T item : list) {
                map = new HashMap<>();
                Class<?> clazz = item.getClass();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = field.get(item);
                    map.put(fieldName, value);
                }
                result.add(map);
            }
            return result;
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }
}
