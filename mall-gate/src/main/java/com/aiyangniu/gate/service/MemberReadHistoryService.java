package com.aiyangniu.gate.service;

import com.aiyangniu.gate.domain.MemberReadHistory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 会员浏览记录管理接口
 *
 * @author lzq
 * @date 2024/02/05
 */
public interface MemberReadHistoryService {

    /**
     * 生成浏览记录
     *
     * @param memberReadHistory 浏览记录
     * @return 生成记录数
     */
    int create(MemberReadHistory memberReadHistory);

    /**
     * 批量删除浏览记录
     *
     * @param ids 记录ID
     * @return 删除个数
     */
    int delete(List<String> ids);

    /**
     * 分页获取用户浏览历史记录
     *
     * @param pageNum 起始页
     * @param pageSize 页条数
     * @return 用户浏览历史记录
     */
    Page<MemberReadHistory> list(Integer pageNum, Integer pageSize);

    /**
     * 清空浏览记录
     */
    void clear();
}
