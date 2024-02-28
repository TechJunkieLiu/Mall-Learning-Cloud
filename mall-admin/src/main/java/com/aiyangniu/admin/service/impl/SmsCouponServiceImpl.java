package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.SmsCouponMapper;
import com.aiyangniu.admin.mapper.SmsCouponProductCategoryRelationMapper;
import com.aiyangniu.admin.mapper.SmsCouponProductRelationMapper;
import com.aiyangniu.admin.service.SmsCouponService;
import com.aiyangniu.entity.model.dto.SmsCouponParamDTO;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductCategoryRelation;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券管理实现类
 *
 * @author lzq
 * @date 2024/02/28
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsCouponServiceImpl implements SmsCouponService {

    private final SmsCouponMapper smsCouponMapper;
    private final SmsCouponProductRelationMapper smsCouponProductRelationMapper;
    private final SmsCouponProductCategoryRelationMapper smsCouponProductCategoryRelationMapper;

    @Override
    public int create(SmsCouponParamDTO couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        // 插入优惠券表
        int count = smsCouponMapper.insert(couponParam);
        // 插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation : couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            smsCouponProductRelationMapper.insertList(couponParam.getProductRelationList());
        }
        // 插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            smsCouponProductCategoryRelationMapper.insertList(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    @Override
    public int delete(Long id) {
        // 删除优惠券
        int count = smsCouponMapper.deleteById(id);
        // 删除商品关联
        smsCouponProductRelationMapper.delete(new LambdaQueryWrapper<SmsCouponProductRelation>().eq(SmsCouponProductRelation::getCouponId, id));
        // 删除商品分类关联
        smsCouponProductCategoryRelationMapper.delete(new LambdaQueryWrapper<SmsCouponProductCategoryRelation>().eq(SmsCouponProductCategoryRelation::getCouponId, id));
        return count;
    }

    @Override
    public int update(Long id, SmsCouponParamDTO couponParam) {
        couponParam.setId(id);
        int count = smsCouponMapper.updateById(couponParam);
        // 删除后插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation : couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            smsCouponProductRelationMapper.delete(new LambdaQueryWrapper<SmsCouponProductRelation>().eq(SmsCouponProductRelation::getCouponId, id));
            smsCouponProductRelationMapper.insertList(couponParam.getProductRelationList());
        }
        // 删除后插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            smsCouponProductCategoryRelationMapper.delete(new LambdaQueryWrapper<SmsCouponProductCategoryRelation>().eq(SmsCouponProductCategoryRelation::getCouponId, id));
            smsCouponProductCategoryRelationMapper.insertList(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    @Override
    public List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return smsCouponMapper.selectList(new LambdaQueryWrapper<SmsCoupon>().like(!StrUtil.isEmpty(name), SmsCoupon::getName, name).eq(type != null, SmsCoupon::getType, type));
    }

    @Override
    public SmsCouponParamDTO getItem(Long id) {
        return smsCouponMapper.getItem(id);
    }
}
