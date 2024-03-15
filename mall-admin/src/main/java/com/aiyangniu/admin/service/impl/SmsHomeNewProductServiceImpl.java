package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.SmsHomeNewProductMapper;
import com.aiyangniu.admin.service.SmsHomeNewProductService;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeNewProduct;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页新品推荐管理实现类
 *
 * @author lzq
 * @date 2024/03/15
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHomeNewProductServiceImpl implements SmsHomeNewProductService {

    private final SmsHomeNewProductMapper homeNewProductMapper;

    @Override
    public int create(List<SmsHomeNewProduct> homeNewProductList) {
        for (SmsHomeNewProduct smsHomeNewProduct : homeNewProductList) {
            smsHomeNewProduct.setRecommendStatus(1);
            smsHomeNewProduct.setSort(0);
        }
        return homeNewProductMapper.insertList(homeNewProductList);
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        return homeNewProductMapper.updateById(homeNewProduct);
    }

    @Override
    public int delete(List<Long> ids) {
        return homeNewProductMapper.delete(new LambdaQueryWrapper<SmsHomeNewProduct>().in(SmsHomeNewProduct::getId, ids));
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeNewProduct record = new SmsHomeNewProduct();
        record.setRecommendStatus(recommendStatus);
        return homeNewProductMapper.update(record, new LambdaQueryWrapper<SmsHomeNewProduct>().in(SmsHomeNewProduct::getId, ids));
    }

    @Override
    public List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return homeNewProductMapper.selectList(new LambdaQueryWrapper<SmsHomeNewProduct>()
                .like(!StrUtil.isEmpty(productName), SmsHomeNewProduct::getProductName, productName)
                .eq(recommendStatus != null, SmsHomeNewProduct::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeNewProduct::getSort)
        );
    }
}
