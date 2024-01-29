package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.dto.PmsBrandParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品品牌管理接口
 *
 * @author lzq
 * @date 2024/01/26
 */
public interface PmsBrandService {

    /**
     * 获取所有品牌
     *
     * @return 品牌列表
     */
    List<PmsBrand> listAllBrand();

    /**
     * 创建品牌
     *
     * @param dto 品牌请求参数
     * @return 创建个数
     */
    int createBrand(PmsBrandParamDTO dto);

    /**
     * 修改品牌
     *
     * @param id 品牌ID
     * @param dto 品牌请求参数
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int updateBrand(Long id, PmsBrandParamDTO dto);

    /**
     * 删除品牌
     *
     * @param id 品牌ID
     * @return 删除个数
     */
    int deleteBrand(Long id);

    /**
     * 批量删除品牌
     *
     * @param ids 品牌IDS
     * @return 删除个数
     */
    int deleteBrand(List<Long> ids);

    /**
     * 分页查询品牌
     *
     * @param keyword 关键字
     * @param showStatus 显示状态
     * @param pageNum 当前页
     * @param pageSize 页条数
     * @return 品牌列表
     */
    List<PmsBrand> listBrand(String keyword, Integer showStatus, int pageNum, int pageSize);

    /**
     * 获取品牌详情
     *
     * @param id 品牌ID
     * @return 品牌详情
     */
    PmsBrand getBrand(Long id);

    /**
     * 修改显示状态
     *
     * @param ids 品牌IDS
     * @param showStatus 显示状态
     * @return 修改个数
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 修改厂家制造商状态
     *
     * @param ids 品牌IDS
     * @param factoryStatus 制造商状态
     * @return 修改个数
     */
    int updateFactoryStatus(List<Long> ids, Integer factoryStatus);
}
