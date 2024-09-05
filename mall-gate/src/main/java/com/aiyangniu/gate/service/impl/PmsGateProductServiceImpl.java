package com.aiyangniu.gate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.gate.mapper.PmsGateProductMapper;
import com.aiyangniu.gate.service.PmsGateProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 前台商品管理实现类
 *
 * @author lzq
 * @date 2024/08/30
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsGateProductServiceImpl implements PmsGateProductService {

    private final PmsGateProductMapper pmsGateProductMapper;

    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lqw = new LambdaQueryWrapper();
        lqw.like(StrUtil.isNotEmpty(keyword), PmsProduct::getName, keyword);
        lqw.eq(brandId != null, PmsProduct::getBrandId, brandId);
        lqw.eq(productCategoryId != null, PmsProduct::getProductCategoryId, productCategoryId);
        // 1->按新品；2->按销量；3->价格从低到高；4->价格从高到低
        switch (sort){
            case 1:
                lqw.orderByDesc(PmsProduct::getId);
                break;
            case 2:
                lqw.orderByDesc(PmsProduct::getSale);
                break;
            case 3:
                lqw.orderByAsc(PmsProduct::getPrice);
                break;
            case 4:
                lqw.orderByDesc(PmsProduct::getPrice);
                break;
            default:
                break;
        }
        return pmsGateProductMapper.selectList(lqw);
    }

}
