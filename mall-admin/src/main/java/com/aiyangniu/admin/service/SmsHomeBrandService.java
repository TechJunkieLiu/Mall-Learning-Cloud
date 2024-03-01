package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页品牌推荐管理接口
 *
 * @author lzq
 * @date 2024/03/01
 */
public interface SmsHomeBrandService {

    /**
     * 批量添加首页品牌推荐
     *
     * @param homeBrandList 首页品牌推荐列表
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(List<SmsHomeBrand> homeBrandList);

    /**
     * 修改品牌推荐排序
     *
     * @param id 品牌推荐ID
     * @param sort 品牌推荐排序
     * @return 修改个数
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量删除品牌推荐
     *
     * @param ids 品牌推荐IDS
     * @return 删除个数
     */
    int delete(List<Long> ids);

    /**
     * 批量更新推荐状态
     *
     * @param ids 品牌推荐IDS
     * @param recommendStatus 品牌推荐状态
     * @return 更新个数
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询品牌推荐
     *
     * @param brandName 品牌名称
     * @param recommendStatus 品牌推荐状态
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 首页推荐品牌分页列表
     */
    List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
