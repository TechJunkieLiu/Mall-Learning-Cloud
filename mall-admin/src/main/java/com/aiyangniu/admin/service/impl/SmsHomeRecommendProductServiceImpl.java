package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.SmsHomeRecommendProductMapper;
import com.aiyangniu.admin.service.SmsHomeRecommendProductService;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeRecommendProduct;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页人气推荐管理实现类
 *
 * @author lzq
 * @date 2024/03/15
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHomeRecommendProductServiceImpl implements SmsHomeRecommendProductService {

    private final SmsHomeRecommendProductMapper recommendProductMapper;

    @Override
    public int create(List<SmsHomeRecommendProduct> homeRecommendProductList) {
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProductList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
        }
        return recommendProductMapper.insertList(homeRecommendProductList);
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct recommendProduct = new SmsHomeRecommendProduct();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return recommendProductMapper.updateById(recommendProduct);
    }

    @Override
    public int delete(List<Long> ids) {
        return recommendProductMapper.delete(new LambdaQueryWrapper<SmsHomeRecommendProduct>().in(SmsHomeRecommendProduct::getId, ids));
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeRecommendProduct record = new SmsHomeRecommendProduct();
        record.setRecommendStatus(recommendStatus);
        return recommendProductMapper.update(record, new LambdaQueryWrapper<SmsHomeRecommendProduct>().in(SmsHomeRecommendProduct::getId, ids));
    }

    @Override
    public List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return recommendProductMapper.selectList(new LambdaQueryWrapper<SmsHomeRecommendProduct>()
                .like(!StrUtil.isEmpty(productName), SmsHomeRecommendProduct::getProductName, productName)
                .eq(recommendStatus != null, SmsHomeRecommendProduct::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeRecommendProduct::getSort)
        );
    }
}
