package com.aiyangniu.demo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌导入、导出、模板
 *
 * @author lzq
 * @date 2023/11/06
 */
@Data
@ApiModel(value = "pms_brand", description = "品牌导入、导出、模板")
public class PmsBrandForEasyExcel implements Serializable {

    private static final long serialVersionUID = 1735684341898147468L;

    @ColumnWidth(25)
    @ExcelProperty(value = "品牌名称", index = 0)
    private String name;

    @ExcelProperty(value = "首字母", index = 1)
    private String firstLetter;

    @ExcelProperty(value = "排序", index = 2)
    private Integer sort;

    @ExcelProperty(value = "是否为品牌制造商", index = 3)
    private Integer factoryStatus;

    @ExcelProperty(value = "展示状态", index = 4)
    private Integer showStatus;

    @ExcelProperty(value = "产品数量", index = 5)
    private Integer productCount;

    @ExcelProperty(value = "产品评论数量", index = 6)
    private Integer productCommentCount;

    @ExcelProperty(value = "品牌logo", index = 7)
    private String logo;

    @ExcelProperty(value = "专区大图", index = 8)
    private String bigPic;

    @ExcelProperty(value = "品牌故事", index = 9)
    private String brandStory;
}
