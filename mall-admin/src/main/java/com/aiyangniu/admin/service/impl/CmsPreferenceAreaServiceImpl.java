package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.CmsPreferenceAreaMapper;
import com.aiyangniu.admin.service.CmsPreferenceAreaService;
import com.aiyangniu.entity.model.pojo.cms.CmsPreferenceArea;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品优选管理实现类
 *
 * @author lzq
 * @date 2024/01/09
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CmsPreferenceAreaServiceImpl implements CmsPreferenceAreaService {

    private final CmsPreferenceAreaMapper cmsPreferenceAreaMapper;

    @Override
    public List<CmsPreferenceArea> listAll() {
        return cmsPreferenceAreaMapper.selectList(new LambdaQueryWrapper<>());
    }
}
