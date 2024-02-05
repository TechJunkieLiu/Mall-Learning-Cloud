package com.aiyangniu.gate.repository;

import com.aiyangniu.gate.domain.MemberProductCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 会员商品收藏操作类
 *
 * @author lzq
 * @date 2024/02/05
 */
public interface MemberCollectionRepository extends MongoRepository<MemberProductCollection, String> {

    /**
     * 根据会员ID和商品ID查找记录
     *
     * @param memberId 会员ID
     * @param productId 商品ID
     * @return 会员商品收藏记录
     */
    MemberProductCollection findByMemberIdAndProductId(Long memberId, Long productId);

    /**
     * 根据会员ID和商品ID删除记录
     *
     * @param memberId 会员ID
     * @param productId 商品ID
     * @return 删除个数
     */
    int deleteByMemberIdAndProductId(Long memberId, Long productId);

    /**
     * 根据会员ID分页查询记录
     *
     * @param memberId 会员ID
     * @param pageable 分页参数
     * @return 分页会员商品收藏列表
     */
    Page<MemberProductCollection> findByMemberId(Long memberId, Pageable pageable);

    /**
     * 根据会员ID删除记录
     *
     * @param memberId 会员ID
     */
    void deleteAllByMemberId(Long memberId);
}
