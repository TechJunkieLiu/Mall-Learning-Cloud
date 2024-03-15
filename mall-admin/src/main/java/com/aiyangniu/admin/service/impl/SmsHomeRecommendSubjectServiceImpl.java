package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.SmsHomeRecommendSubjectMapper;
import com.aiyangniu.admin.service.SmsHomeRecommendSubjectService;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeRecommendSubject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页专题推荐管理实现类
 *
 * @author lzq
 * @date 2024/03/15
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHomeRecommendSubjectServiceImpl implements SmsHomeRecommendSubjectService {

    private final SmsHomeRecommendSubjectMapper smsHomeRecommendSubjectMapper;

    @Override
    public int create(List<SmsHomeRecommendSubject> recommendSubjectList) {
        for (SmsHomeRecommendSubject recommendSubject : recommendSubjectList) {
            recommendSubject.setRecommendStatus(1);
            recommendSubject.setSort(0);
        }
        return smsHomeRecommendSubjectMapper.insertList(recommendSubjectList);
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendSubject recommendSubject = new SmsHomeRecommendSubject();
        recommendSubject.setId(id);
        recommendSubject.setSort(sort);
        return smsHomeRecommendSubjectMapper.updateById(recommendSubject);
    }

    @Override
    public int delete(List<Long> ids) {
        return smsHomeRecommendSubjectMapper.delete(new LambdaQueryWrapper<SmsHomeRecommendSubject>().in(SmsHomeRecommendSubject::getId, ids));
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeRecommendSubject record = new SmsHomeRecommendSubject();
        record.setRecommendStatus(recommendStatus);
        return smsHomeRecommendSubjectMapper.update(record, new LambdaQueryWrapper<SmsHomeRecommendSubject>().in(SmsHomeRecommendSubject::getId, ids));
    }

    @Override
    public List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return smsHomeRecommendSubjectMapper.selectList(new LambdaQueryWrapper<SmsHomeRecommendSubject>()
                .like(!StrUtil.isEmpty(subjectName), SmsHomeRecommendSubject::getSubjectName, subjectName)
                .eq(recommendStatus != null, SmsHomeRecommendSubject::getRecommendStatus, recommendStatus)
                .orderByDesc(SmsHomeRecommendSubject::getSort)
        );
    }
}
