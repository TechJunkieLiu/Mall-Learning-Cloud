package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.pms.PmsBrand;

import java.util.List;

/**
 * 商品品牌管理接口
 *
 * @author lzq
 * @date 2023/09/27
 */
public interface PmsBrandService {

    /**
     * 获取所有品牌
     *
     * @return 品牌列表
     */
    List<PmsBrand> listAllBrand();
}
