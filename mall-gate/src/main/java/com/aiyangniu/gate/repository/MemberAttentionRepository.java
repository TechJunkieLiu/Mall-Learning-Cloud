package com.aiyangniu.gate.repository;

import com.aiyangniu.gate.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 会员品牌关注操作类
 *
 * @author lzq
 * @date 2024/02/04
 */
public interface MemberAttentionRepository extends MongoRepository<MemberBrandAttention, String> {

    /**
     * 根据会员ID和品牌ID查找记录
     *
     * @param memberId 会员ID
     * @param brandId 品牌ID
     * @return 会员品牌关注记录
     */
    MemberBrandAttention findByMemberIdAndBrandId(Long memberId, Long brandId);

    /**
     * 根据会员ID和品牌ID删除记录
     *
     * @param memberId 会员ID
     * @param brandId 品牌ID
     * @return 删除个数
     */
    int deleteByMemberIdAndBrandId(Long memberId, Long brandId);

    /**
     * 根据会员ID分页查找记录
     *
     * @param memberId 会员ID
     * @param pageable 分页参数
     * @return 分页会员品牌关注列表
     */
    Page<MemberBrandAttention> findByMemberId(Long memberId, Pageable pageable);

    /**
     * 根据会员ID删除记录
     *
     * @param memberId 会员ID
     */
    void deleteAllByMemberId(Long memberId);
}
