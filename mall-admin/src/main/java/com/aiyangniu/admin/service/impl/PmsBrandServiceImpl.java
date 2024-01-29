package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.PmsBrandMapper;
import com.aiyangniu.admin.mapper.PmsProductMapper;
import com.aiyangniu.admin.service.PmsBrandService;
import com.aiyangniu.entity.model.dto.PmsBrandParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品品牌管理实现类
 *
 * @author lzq
 * @date 2024/01/26
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsBrandServiceImpl implements PmsBrandService {

    private final PmsBrandMapper pmsBrandMapper;
    private final PmsProductMapper pmsProductMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return pmsBrandMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public int createBrand(PmsBrandParamDTO pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        // 如果创建时首字母为空，取名称的第一个为首字母
        if (StrUtil.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        return pmsBrandMapper.insert(pmsBrand);
    }

    @Override
    public int updateBrand(Long id, PmsBrandParamDTO pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);
        // 如果创建时首字母为空，取名称的第一个为首字母
        if (StrUtil.isEmpty(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }
        // 更新品牌时要更新商品中的品牌名称
        PmsProduct product = new PmsProduct();
        product.setBrandName(pmsBrand.getName());
        pmsProductMapper.update(product, new LambdaQueryWrapper<PmsProduct>().eq(PmsProduct::getBrandId, id));
        return pmsBrandMapper.updateById(pmsBrand);
    }

    @Override
    public int deleteBrand(Long id) {
        return pmsBrandMapper.deleteById(id);
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        return pmsBrandMapper.delete(new LambdaQueryWrapper<PmsBrand>().in(PmsBrand::getId, ids));
    }

    @Override
    public List<PmsBrand> listBrand(String keyword, Integer showStatus, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return pmsBrandMapper.selectList(new LambdaQueryWrapper<PmsBrand>().orderByDesc(PmsBrand::getSort).like(!StrUtil.isEmpty(keyword), PmsBrand::getName, keyword).eq(showStatus != null, PmsBrand::getShowStatus, showStatus));
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return pmsBrandMapper.selectById(id);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);
        return pmsBrandMapper.update(pmsBrand, new LambdaQueryWrapper<PmsBrand>().in(PmsBrand::getId, ids));
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setFactoryStatus(factoryStatus);
        return pmsBrandMapper.update(pmsBrand, new LambdaQueryWrapper<PmsBrand>().in(PmsBrand::getId, ids));
    }
}
