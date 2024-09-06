package com.aiyangniu.gate.service.impl;

import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.gate.mapper.HomeMapper;
import com.aiyangniu.gate.mapper.PmsBrandMapper;
import com.aiyangniu.gate.mapper.PmsProductMapper;
import com.aiyangniu.gate.service.PmsGateBrandService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 前台商品品牌管理实现类
 *
 * @author lzq
 * @date 2024/09/05
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsGateBrandServiceImpl implements PmsGateBrandService {

    private final HomeMapper homeMapper;
    private final PmsBrandMapper brandMapper;
    private final PmsProductMapper productMapper;

    @Override
    public List<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return homeMapper.getRecommendBrandList(offset, pageSize);
    }

    @Override
    public PmsBrand detail(Long brandId) {
        return brandMapper.selectById(brandId);
    }

    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PmsProduct> productList = productMapper.selectList(new LambdaQueryWrapper<PmsProduct>()
                .eq(PmsProduct::getDeleteStatus, 0)
                .eq(PmsProduct::getPublishStatus, 1)
                .eq(PmsProduct::getBrandId, brandId));
        return CommonPage.restPage(productList);
    }
}
