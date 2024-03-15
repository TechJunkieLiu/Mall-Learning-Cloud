package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeRecommendSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页专题推荐管理Mapper
 *
 * @author lzq
 * @date 2024/03/15
 */
public interface SmsHomeRecommendSubjectMapper extends BaseMapper<SmsHomeRecommendSubject> {

    /**
     * 批量添加首页专题推荐
     *
     * @param list 首页专题推荐列表
     * @return 添加数量
     */
    int insertList(@Param("list") List<SmsHomeRecommendSubject> list);
}
