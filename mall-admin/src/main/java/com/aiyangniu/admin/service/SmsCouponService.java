package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.dto.SmsCouponParamDTO;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 优惠券管理接口
 *
 * @author lzq
 * @date 2024/02/28
 */
public interface SmsCouponService {

    /**
     * 添加优惠券
     *
     * @param couponParam 优惠券信息封装，包括绑定商品和分类
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(SmsCouponParamDTO couponParam);

    /**
     * 根据优惠券id删除优惠券
     *
     * @param id 优惠券ID
     * @return 删除个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int delete(Long id);

    /**
     * 根据优惠券id更新优惠券信息
     *
     * @param id 优惠券ID
     * @param couponParam 优惠券信息封装，包括绑定商品和分类
     * @return 更新个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int update(Long id, SmsCouponParamDTO couponParam);

    /**
     * 分页获取优惠券列表
     *
     * @param name 优惠券名称
     * @param type 优惠券类型
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 优惠券列表
     */
    List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 获取优惠券详情
     *
     * @param id 优惠券ID
     * @return 优惠券详情
     */
    SmsCouponParamDTO getItem(Long id);
}
