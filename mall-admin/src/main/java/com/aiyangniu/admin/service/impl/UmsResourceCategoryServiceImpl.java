package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.UmsResourceCategoryMapper;
import com.aiyangniu.admin.service.UmsResourceCategoryService;
import com.aiyangniu.entity.model.pojo.ums.UmsResourceCategory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 后台资源分类管理实现类
 *
 * @author lzq
 * @date 2024/02/27
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {

    private final UmsResourceCategoryMapper umsResourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        return umsResourceCategoryMapper.selectList(new LambdaQueryWrapper<UmsResourceCategory>().orderByDesc(UmsResourceCategory::getSort));
    }

    @Override
    public int create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        return umsResourceCategoryMapper.insert(umsResourceCategory);
    }

    @Override
    public int update(Long id, UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        return umsResourceCategoryMapper.updateById(umsResourceCategory);
    }

    @Override
    public int delete(Long id) {
        return umsResourceCategoryMapper.deleteById(id);
    }
}
