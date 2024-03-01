package com.aiyangniu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.SmsHomeBrandMapper;
import com.aiyangniu.admin.service.SmsHomeBrandService;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeBrand;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页品牌推荐管理实现类
 *
 * @author lzq
 * @date 2024/03/01
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHomeBrandServiceImpl implements SmsHomeBrandService {

    private final SmsHomeBrandMapper smsHomeBrandMapper;

    @Override
    public int create(List<SmsHomeBrand> homeBrandList) {
        List<SmsHomeBrand> list = new ArrayList<>();
        homeBrandList.forEach(e -> {
            SmsHomeBrand smsHomeBrand = new SmsHomeBrand();
            BeanUtil.copyProperties(e, smsHomeBrand);
            smsHomeBrand.setRecommendStatus(1);
            smsHomeBrand.setSort(0);
            list.add(smsHomeBrand);
        });
        return smsHomeBrandMapper.insertList(list);
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeBrand homeBrand = new SmsHomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        return smsHomeBrandMapper.updateById(homeBrand);
    }

    @Override
    public int delete(List<Long> ids) {
        return smsHomeBrandMapper.delete(new LambdaQueryWrapper<SmsHomeBrand>().in(SmsHomeBrand::getId, ids));
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeBrand record = new SmsHomeBrand();
        record.setRecommendStatus(recommendStatus);
        return smsHomeBrandMapper.update(record, new LambdaQueryWrapper<SmsHomeBrand>().in(SmsHomeBrand::getId, ids));
    }

    @Override
    public List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return smsHomeBrandMapper.selectList(new LambdaQueryWrapper<SmsHomeBrand>()
                .like(!StrUtil.isEmpty(brandName), SmsHomeBrand::getBrandName, brandName)
                .eq(recommendStatus != null, SmsHomeBrand::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeBrand::getSort)
        );
    }
}
