package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.cms.CmsPreferenceArea;

import java.util.List;

/**
 * 商品优选管理接口
 *
 * @author lzq
 * @date 2024/01/09
 */
public interface CmsPreferenceAreaService {

    /**
     * 获取所有优选专区
     *
     * @return 优选专区
     */
    List<CmsPreferenceArea> listAll();
}
