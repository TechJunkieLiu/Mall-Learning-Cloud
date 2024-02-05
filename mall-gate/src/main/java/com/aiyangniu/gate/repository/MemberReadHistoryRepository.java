package com.aiyangniu.gate.repository;

import com.aiyangniu.gate.domain.MemberReadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 会员商品浏览历史操作类
 *
 * @author lzq
 * @date 2024/02/05
 */
public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory, String> {

    /**
     * 根据会员ID分页查找记录
     *
     * @param memberId 会员ID
     * @param pageable 分页信息
     * @return 分页记录信息
     */
    Page<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);

    /**
     * 根据会员ID删除记录
     *
     * @param memberId 会员ID
     */
    void deleteAllByMemberId(Long memberId);
}
