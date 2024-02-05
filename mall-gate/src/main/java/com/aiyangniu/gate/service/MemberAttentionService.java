package com.aiyangniu.gate.service;

import com.aiyangniu.gate.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;

/**
 * 会员品牌关注管理接口
 *
 * @author lzq
 * @date 2024/02/04
 */
public interface MemberAttentionService {

    /**
     * 添加关注
     *
     * @param memberBrandAttention 会员品牌关注
     * @return 添加数量
     */
    int add(MemberBrandAttention memberBrandAttention);

    /**
     * 取消关注
     *
     * @param brandId 会员品牌关注ID
     * @return 取消数量
     */
    int delete(Long brandId);

    /**
     * 分页获取用户关注列表
     *
     * @param pageNum 起始页
     * @param pageSize 页条数
     * @return 分页用户关注列表
     */
    Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize);

    /**
     * 获取用户关注详情
     *
     * @param brandId 会员品牌关注ID
     * @return 会员品牌关注详情
     */
    MemberBrandAttention detail(Long brandId);

    /**
     * 清空关注列表
     */
    void clear();
}
