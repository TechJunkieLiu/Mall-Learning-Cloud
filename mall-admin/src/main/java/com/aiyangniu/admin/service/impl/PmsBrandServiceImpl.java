package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.PmsBrandMapper;
import com.aiyangniu.admin.service.PmsBrandService;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品品牌管理实现类
 *
 * @author lzq
 * @date 2023/09/27
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsBrandServiceImpl implements PmsBrandService {

    private final PmsBrandMapper pmsBrandMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return pmsBrandMapper.selectList(new LambdaQueryWrapper<>());
    }
}
