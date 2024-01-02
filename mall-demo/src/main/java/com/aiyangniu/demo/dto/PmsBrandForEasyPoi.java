package com.aiyangniu.demo.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌导入、导出
 *
 * @author lzq
 * @date 2023/11/06
 */
@Data
@ApiModel(value = "pms_brand", description = "品牌导入、导出")
public class PmsBrandForEasyPoi implements Serializable {

    private static final long serialVersionUID = 1735684341898147468L;

    @Excel(name = "品牌名称", orderNum = "0", isImportField = "false", width = 30)
    private String name;

    @Excel(name = "首字母", orderNum = "1")
    private String firstLetter;

    @Excel(name = "排序", orderNum = "2")
    private Integer sort;

    @Excel(name = "是否为品牌制造商", orderNum = "3")
    private Integer factoryStatus;

    @Excel(name = "展示状态", orderNum = "4")
    private Integer showStatus;

    @Excel(name = "产品数量", orderNum = "5")
    private Integer productCount;

    @Excel(name = "产品评论数量", orderNum = "6")
    private Integer productCommentCount;

    @Excel(name = "品牌logo", orderNum = "7")
    private String logo;

    @Excel(name = "专区大图", orderNum = "8")
    private String bigPic;

    @Excel(name = "品牌故事", orderNum = "9")
    private String brandStory;
}
