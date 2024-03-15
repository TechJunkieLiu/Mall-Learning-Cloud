package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeRecommendSubject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页专题推荐管理接口
 *
 * @author lzq
 * @date 2024/03/15
 */
public interface SmsHomeRecommendSubjectService {

    /**
     * 添加首页推荐
     *
     * @param recommendSubjectList 首页推荐列表
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(List<SmsHomeRecommendSubject> recommendSubjectList);

    /**
     * 修改推荐排序
     *
     * @param id 专题推荐ID
     * @param sort 专题推荐排序
     * @return 修改个数
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量删除推荐
     *
     * @param ids 专题推荐IDS
     * @return 删除个数
     */
    int delete(List<Long> ids);

    /**
     * 批量更新推荐状态
     *
     * @param ids 专题推荐IDS
     * @param recommendStatus 专题推荐状态
     * @return 更新个数
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询推荐
     *
     * @param subjectName 专题名称
     * @param recommendStatus 专题推荐状态
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 首页推荐专题分页列表
     */
    List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
