package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.PmsProductResult;
import com.aiyangniu.entity.model.dto.PmsProductParamDTO;
import com.aiyangniu.entity.model.dto.PmsProductQueryParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品管理接口
 *
 * @author lzq
 * @date 2024/01/26
 */
public interface PmsProductService {

    /**
     * 创建商品
     *
     * @param dto 商品请求参数
     * @return 创建个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class}, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    int create(PmsProductParamDTO dto);

    /**
     * 根据商品编号获取更新信息
     *
     * @param id 商品ID
     * @return 更新信息
     */
    PmsProductResult getUpdateInfo(Long id);

    /**
     * 更新商品
     *
     * @param id 商品ID
     * @param dto 商品请求参数
     * @return 更新个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int update(Long id, PmsProductParamDTO dto);

    /**
     * 分页查询商品
     *
     * @param dto 商品查询参数
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 商品列表
     */
    List<PmsProduct> list(PmsProductQueryParamDTO dto, Integer pageSize, Integer pageNum);

    /**
     * 批量修改审核状态
     *
     * @param ids 商品IDS
     * @param verifyStatus 审核状态
     * @param detail 审核详情
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     *
     * @param ids 商品IDS
     * @param publishStatus 上架状态
     * @return 修改个数
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     *
     * @param ids 商品IDS
     * @param recommendStatus 推荐状态
     * @return 修改个数
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     *
     * @param ids 商品IDS
     * @param newStatus 新品状态
     * @return 修改个数
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品
     *
     * @param ids 商品IDS
     * @param deleteStatus 删除状态
     * @return 删除个数
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     *
     * @param keyword 关键字
     * @return 商品列表
     */
    List<PmsProduct> list(String keyword);
}
