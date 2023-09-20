package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页品牌推荐管理Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface SmsHomeBrandMapper extends BaseMapper<SmsHomeBrand> {

    /**
     * 批量添加首页品牌推荐
     *
     * @param list 首页品牌推荐列表
     * @return 添加数量
     */
    int insertList(@Param("list") List<SmsHomeBrand> list);
}
