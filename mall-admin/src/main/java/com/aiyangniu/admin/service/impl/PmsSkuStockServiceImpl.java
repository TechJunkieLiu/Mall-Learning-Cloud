package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.PmsSkuStockMapper;
import com.aiyangniu.admin.service.PmsSkuStockService;
import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品SKU库存管理实现类
 *
 * @author lzq
 * @date 2024/01/31
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    private final PmsSkuStockMapper pmsSkuStockMapper;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        return pmsSkuStockMapper.selectList(new LambdaQueryWrapper<PmsSkuStock>().eq(PmsSkuStock::getProductId, pid).like(!StrUtil.isEmpty(keyword), PmsSkuStock::getSkuCode, keyword));
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return pmsSkuStockMapper.replaceList(skuStockList);
    }
}
