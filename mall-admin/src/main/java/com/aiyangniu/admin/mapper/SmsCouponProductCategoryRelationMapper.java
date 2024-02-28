package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductCategoryRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券关联商品分类Mapper
 *
 * @author lzq
 * @date 2024/02/28
 */
public interface SmsCouponProductCategoryRelationMapper extends BaseMapper<SmsCouponProductCategoryRelation> {

    /**
     * 批量创建
     *
     * @param productCategoryRelationList 优惠券和产品分类关系列表
     * @return 创建个数
     */
    int insertList(@Param("list") List<SmsCouponProductCategoryRelation> productCategoryRelationList);
}
