package com.aiyangniu.common.utils.ExcelUtils.easyexcel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel导入导出工具类
 *
 * @author lzq
 * @date 2023/08/17
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导出数据为excel文件
     *
     * @param fileName 文件名称
     * @param dataResult 集合内的bean对象类型要与clazz参数一致
     * @param clazz 集合内的bean对象类型要与clazz参数一致
     * @param response HttpServlet响应对象
     */
    public static void export(String fileName, List<?> dataResult, Class<?> clazz, HttpServletResponse response) {
        response.setStatus(200);
        OutputStream outputStream = null;
        ExcelWriter excelWriter = null;
        try {
            if (StrUtil.isBlank(fileName)) {
                throw new RuntimeException("'fileName' 不能为空");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName.concat(".xlsx"), "utf-8"));
            outputStream = response.getOutputStream();

            // 根据不同的策略生成不同的ExcelWriter对象
            if (dataResult == null){
                excelWriter = getTemplateExcelWriter(outputStream);
            } else {
                excelWriter = getExportExcelWriter(outputStream);
            }

            WriteTable writeTable = EasyExcel.writerTable(0).head(clazz).needHead(true).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            // 写出数据
            excelWriter.write(dataResult, writeSheet, writeTable);

        } catch (Exception e) {
            LOGGER.error("导出excel数据异常：", e);
            throw new RuntimeException(e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.error("导出数据关闭流异常", e);
                }
            }
        }
    }

    /**
     * 根据不同策略生成不同的ExcelWriter对象，可根据实际情况修改
     *
     * @param outputStream 数据输出流
     * @return 模板下载ExcelWriter对象
     */
    private static ExcelWriter getTemplateExcelWriter(OutputStream outputStream){
        return EasyExcel.write(outputStream)
                // 增加批注策略
                .registerWriteHandler(new CommentWriteHandler())
                // 增加下拉框策略
                .registerWriteHandler(new CustomSheetWriteHandler())
                // 字体居中策略
                .registerWriteHandler(getStyleStrategy())
                .build();
    }

    /**
     * 根据不同策略生成不同的ExcelWriter对象， 可根据实际情况修改
     *
     * @param outputStream 数据输出流
     * @return 数据导出ExcelWriter对象
     */
    private static ExcelWriter getExportExcelWriter(OutputStream outputStream){
        return EasyExcel.write(outputStream)
                // 字体居中策略
                .registerWriteHandler(getStyleStrategy())
                .build();
    }

    /**
     * 设置表格内容居中显示策略
     */
    private static HorizontalCellStyleStrategy getStyleStrategy(){
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容策略
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        // 设置内容水平居中
        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, writeCellStyle);
    }

    /**
     * 根据Excel模板，批量导入数据
     *
     * @param file  导入的Excel
     * @param clazz 解析的类型
     * @return  解析完成的数据
     */
    public static List<?> importExcel(MultipartFile file, Class<?> clazz){
        if (file == null || file.isEmpty()){
            throw new RuntimeException("没有文件或者文件内容为空！");
        }
        List<Object> dataList = null;
        BufferedInputStream ipt = null;
        try {
            InputStream is = file.getInputStream();
            // 用缓冲流对数据流进行包装
            ipt = new BufferedInputStream(is);
            // 数据解析监听器
            ExcelListener<Object> listener = new ExcelListener<>();
            // 读取数据
            EasyExcel.read(ipt, clazz, listener).sheet().doRead();
            // 获取去读完成之后的数据
            dataList = listener.getDataList();
        } catch (Exception e){
            LOGGER.error(String.valueOf(e));
            throw new RuntimeException("数据导入失败！" + e);
        }
        return dataList;
    }
}
