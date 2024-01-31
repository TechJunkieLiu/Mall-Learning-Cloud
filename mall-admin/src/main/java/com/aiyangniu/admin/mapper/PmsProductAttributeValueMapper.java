package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.pms.PmsProductAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品参数及自定义规格属性Mapper
 *
 * @author lzq
 * @date 2024/01/29
 */
public interface PmsProductAttributeValueMapper extends BaseMapper<PmsProductAttributeValue> {

    /**
     * 批量创建
     *
     * @param productAttributeValueList 商品参数及自定义规格属性列表
     * @return 创建个数
     */
    int insertList(@Param("list") List<PmsProductAttributeValue> productAttributeValueList);
}
