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
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.parse.ParseWord07;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Excel导入导出工具类
 *
 * @author lzq
 * @date 2023/08/22
 */
public class ExcelUtil {

    /**
     * 批量导出
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 批量导出
     *
     * @param exportParams 导出参数（标题、sheet名称、是否创建表头、表格类型）
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response) throws IOException {
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 批量导出
     *
     * @param title 表格内数据标题
     * @param sheetName sheet名称
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName, ExcelType.XSSF), pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 批量导出
     *
     * @param title 表格内数据标题
     * @param sheetName sheet名称
     * @param isCreateHeader 是否创建表头
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        // 把数据添加到excel表格中
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
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
    public static void WordTemplateExportMorePage(List<Map<String, Object>> list, String templatePath, String fileName, HttpServletResponse response) throws Exception {
        XWPFDocument doc = new ParseWord07().parseWord(templatePath, list);
        downLoadWord(fileName, response, doc);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private static void downLoadWord(String fileName, HttpServletResponse response, XWPFDocument doc) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/msword");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".docx", "UTF-8"));
            doc.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 批量导入
     *
     * @param file 文件
     * @param titleRows 表格内数据标题行
     * @param headerRows 表头行
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            InputStream inputStream = file.getInputStream();
            ImportParams params = new ImportParams();
            params.setTitleRows(titleRows);
            params.setHeadRows(headerRows);
            params.setSaveUrl("/excel/");
            params.setNeedSave(true);
            try {
                return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
            } catch (NoSuchElementException e) {
                throw new IOException("excel文件不能为空");
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 批量导入（有错误信息）
     *
     * @param file 文件
     * @param titleRows 表格内数据标题行
     * @param headerRows 表头行
     */
    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            InputStream inputStream = file.getInputStream();
            ImportParams params = new ImportParams();
            params.setTitleRows(titleRows);
            params.setHeadRows(headerRows);
            params.setSaveUrl("/excel/");
            params.setNeedSave(true);
            params.setNeedVerify(true);
            try {
                return ExcelImportUtil.importExcelMore(inputStream, pojoClass, params);
            } catch (NoSuchElementException e) {
                throw new IOException("excel文件不能为空");
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 批量导入
     *
     * @param filePath 文件路径
     * @param titleRows 表格内数据标题行
     * @param headerRows 表头行
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StrUtil.isBlank(filePath)) {
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
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel转html预览
     */
    public static void excelToHtml(String filePath, HttpServletResponse response) throws Exception {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile(filePath)), true);
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }
}
