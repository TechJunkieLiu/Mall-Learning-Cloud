package com.aiyangniu.common.utils.ExcelUtils.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * EasyPoi 工具类
 * 包括动态导出列、动态更改表头
 *
 * @author lzq
 * @date 2024/04/24
 */
public class ExcelUtilTwoPlus {

    /**
     * 动态导出列，根据Excel注解获取列的字段注释（表头名）、宽度
     *
     * @param clazz 导出列实体类
     * @param fields 选择要导出的列
     * @param changeHead 要更改表头的列，格式是{"字段1":"更改的表头1","字段2":"更改的表头2"}
     */
    public static List<ExcelExportEntity> dynamicExport(Class<?> clazz, String fields, JSONObject changeHead) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        String[] split = fields.split(",");
        int length = split.length;
        try {
            for (int i = 0; i < length; i++) {
                Field f = clazz.getDeclaredField(split[i]);
                Excel annotation = f.getAnnotation((Excel.class));
                String comment = annotation.name();
                if (changeHead != null && Objects.nonNull(changeHead.get(f.getName()))){
                    comment = changeHead.get(f.getName()).toString();
                }
                Double width = annotation.width();
                ExcelExportEntity entity = new ExcelExportEntity(comment, f.getName(), width.intValue());
                annotationParams(annotation, entity);
                beanList.add(entity);
            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }
        return beanList;
    }

    /**
     * 动态导出列（选择要忽略的列），根据Excel注解获取列的字段注释（表头名）、宽度
     *
     * @param clazz 导出列实体类
     * @param fields 选择要忽略的列
     * @param changeHead 要更改表头的列，格式是{"字段名1":"更改的表头1","字段名2":"更改的表头2"}
     */
    public static List<ExcelExportEntity> dynamicIgnoreExport(Class<?> clazz, String fields, JSONObject changeHead) {
        List<ExcelExportEntity> beanList = new ArrayList<>();
        String[] split = fields.split(",");
        int length = split.length;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            Excel annotation = f.getAnnotation((Excel.class));
            if (annotation != null){
                boolean flag = false;
                for (int i = 0; i < length; i++) {
                    if (f.getName().equals(split[i])){
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                String comment = annotation.name();
                if (changeHead != null && Objects.nonNull(changeHead.get(f.getName()))){
                    comment = changeHead.get(f.getName()).toString();
                }
                Double width = annotation.width();
                ExcelExportEntity entity = new ExcelExportEntity(comment, f.getName(), width.intValue());
                annotationParams(annotation,entity);
                beanList.add(entity);
            }
        }
        return beanList;
    }

    /**
     * 设置注解参数
     *
     * @param annotation 注解
     * @param entity ExcelExportEntity
     */
    private static void annotationParams(Excel annotation, ExcelExportEntity entity){
        if (annotation.addressList()){
            entity.setAddressList(annotation.addressList());
            entity.setReplace(annotation.replace());
        }
        entity.setReplace(annotation.replace());
        entity.setOrderNum(Integer.parseInt(annotation.orderNum()));
        entity.setGroupName(annotation.groupName());
        entity.setNeedMerge(annotation.needMerge());
        entity.setMergeVertical(annotation.mergeVertical());
    }

    /**
     * 导出Excel，并在最后追加图片
     *
     * @param sheetName sheet名称
     * @param wb HSSFWorkbook对象
     */
    public static Workbook getWorkbook(String sheetName, Workbook wb, String imgUrl) throws IOException {
        // 1、创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 2、在workbook中添加一个sheet，对应Excel文件中的sheet
        Sheet sheet = wb.getSheet(sheetName);
        // 生成图表
        if(!StringUtils.isEmpty(imgUrl)) {
            // 拆分base64编码后部分
            String[] imgUrlArr = imgUrl.split("base64,");
            byte[] buffer = new BASE64Decoder().decodeBuffer(imgUrlArr[1]);
            String picPath = System.getProperty("user.dir")+"\\upload\\image\\pic.png";
            File file = new File(picPath);
            try {
                // 生成图片
                OutputStream out = new FileOutputStream(file);
                out.write(buffer);
                out.flush();
                out.close();
                // 将图片写入流中
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                BufferedImage bufferImg = ImageIO.read(new File(picPath));
                ImageIO.write(bufferImg, "PNG", outStream);
                // 利用HSSFPatriarch将图片写入EXCEL
                Drawing drawing = sheet.createDrawingPatriarch();
                // 位置：第1个单元格中x轴的偏移量、第1个单元格中y轴的偏移量、 第2个单元格中x轴的偏移量、 第2个单元格中y轴的偏移量、第1个单元格的列号、第1个单元格的行号、 第2个单元格的列号、第2个单元格的行号
                // HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 8, (short) 10, 40);
                ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 6, 9, 40);
                drawing.createPicture(anchor, wb.addPicture(outStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (file.exists()) {
                file.delete();
            }
        }
        return wb;
    }
}
