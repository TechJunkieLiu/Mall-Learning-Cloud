package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeNewProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页新品推荐管理Mapper
 *
 * @author lzq
 * @date 2024/03/15
 */
public interface SmsHomeNewProductMapper extends BaseMapper<SmsHomeNewProduct> {

    /**
     * 批量添加首页新品推荐
     *
     * @param list 首页新品推荐列表
     * @return 添加数量
     */
    int insertList(@Param("list") List<SmsHomeNewProduct> list);
}
