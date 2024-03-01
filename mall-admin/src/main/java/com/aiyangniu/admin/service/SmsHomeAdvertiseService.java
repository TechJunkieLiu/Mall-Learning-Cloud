package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeAdvertise;

import java.util.List;

/**
 * 首页轮播广告管理接口
 *
 * @author lzq
 * @date 2024/03/01
 */
public interface SmsHomeAdvertiseService {

    /**
     * 添加广告
     *
     * @param advertise 轮播广告
     * @return 添加个数
     */
    int create(SmsHomeAdvertise advertise);

    /**
     * 批量删除广告
     *
     * @param ids 广告IDS
     * @return 删除个数
     */
    int delete(List<Long> ids);

    /**
     * 修改上、下线状态
     *
     * @param id 广告ID
     * @param status 状态
     * @return 修改个数
     */
    int updateStatus(Long id, Integer status);

    /**
     * 获取广告详情
     *
     * @param id 广告ID
     * @return 广告详情
     */
    SmsHomeAdvertise getItem(Long id);

    /**
     * 更新广告
     *
     * @param id 广告ID
     * @param advertise 轮播广告
     * @return 更新
     */
    int update(Long id, SmsHomeAdvertise advertise);

    /**
     * 分页查询广告
     *
     * @param name 广告名称
     * @param type 广告类型
     * @param endTime 结束时间
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 轮播广告列表
     */
    List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);
}
