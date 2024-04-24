package com.aiyangniu.common.utils.ExcelUtils.easypoi;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * Excel导出样式设置工具类
 *
 * @author lzq
 * @date 2024/04/03
 */
public class ExcelStyleUtil implements IExcelExportStyler {

    private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
    private static final short FONT_SIZE_TEN = 12;
    private static final short FONT_SIZE_ELEVEN = 14;
    private static final short FONT_SIZE_TWELVE = 18;

    /** 大标题样式 **/
    private CellStyle headerStyle;

    /** 每列标题样式 **/
    private CellStyle titleStyle;

    /** 数据行样式 **/
    private CellStyle styles;

    public ExcelStyleUtil(Workbook workbook) {
        this.init(workbook);
    }

    /**
     * 初始化样式
     */
    private void init(Workbook workbook) {
        this.headerStyle = initHeaderStyle(workbook);
        this.titleStyle = initTitleStyle(workbook, true, FONT_SIZE_ELEVEN);
        this.styles = initStyles(workbook);
    }

    /**
     * 初始化--大标题样式
     */
    private static CellStyle initHeaderStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_TWELVE, true));
        return style;
    }

    /**
     * 初始化--每列标题样式
     */
    private static CellStyle initTitleStyle(Workbook workbook, boolean isBold, short size) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, size, isBold));
        // 背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 初始化--数据行样式
     */
    private static CellStyle initStyles(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_TEN, false));
        style.setDataFormat(STRING_FORMAT);
        return style;
    }

    /**
     * 基础样式
     */
    private static CellStyle getBaseCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 下边框
        style.setBorderBottom(BorderStyle.THIN);
        // 左边框
        style.setBorderLeft(BorderStyle.THIN);
        // 上边框
        style.setBorderTop(BorderStyle.THIN);
        // 右边框
        style.setBorderRight(BorderStyle.THIN);
        // 水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 字体样式
     *
     * @param size 字体大小
     * @param isBold 是否加粗
     */
    private static Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        // 字体样式
        font.setFontName("宋体");
        // 是否加粗
        font.setBold(isBold);
        // 字体大小
        font.setFontHeightInPoints(size);
        return font;
    }

    @Override
    public CellStyle getHeaderStyle(short i) {
        return headerStyle;
    }

    @Override
    public CellStyle getTitleStyle(short i) {
        return titleStyle;
    }

    @Override
    public CellStyle getTemplateStyles(boolean b, ExcelForEachParams excelForEachParams) {
        return null;
    }

    @Override
    public CellStyle getStyles(boolean b, ExcelExportEntity excelExportEntity) {
        return styles;
    }

    @Override
    public CellStyle getStyles(Cell cell, int i, ExcelExportEntity entity, Object o, Object o1) {
        return getStyles(true, entity);
    }

    /**
     * 设置隔行背景色
     */
    public static CellStyle getStyles(Workbook workbook, boolean isBold, short size) {
        CellStyle style = initTitleStyle(workbook, isBold, size);
        style.setDataFormat(STRING_FORMAT);
        return style;
    }
}
