package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.SmsFlashPromotionSessionMapper;
import com.aiyangniu.admin.service.SmsFlashPromotionProductRelationService;
import com.aiyangniu.admin.service.SmsFlashPromotionSessionService;
import com.aiyangniu.entity.model.bo.SmsFlashPromotionSessionDetail;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionSession;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 限时购场次管理实现类
 *
 * @author lzq
 * @date 2024/03/01
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsFlashPromotionSessionServiceImpl implements SmsFlashPromotionSessionService {

    private final SmsFlashPromotionSessionMapper promotionSessionMapper;
    private final SmsFlashPromotionProductRelationService relationService;

    @Override
    public int create(SmsFlashPromotionSession promotionSession) {
        promotionSession.setCreateTime(new Date());
        return promotionSessionMapper.insert(promotionSession);
    }

    @Override
    public int update(Long id, SmsFlashPromotionSession promotionSession) {
        promotionSession.setId(id);
        return promotionSessionMapper.updateById(promotionSession);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashPromotionSession promotionSession = new SmsFlashPromotionSession();
        promotionSession.setId(id);
        promotionSession.setStatus(status);
        return promotionSessionMapper.updateById(promotionSession);
    }

    @Override
    public int delete(Long id) {
        return promotionSessionMapper.deleteById(id);
    }

    @Override
    public SmsFlashPromotionSession getItem(Long id) {
        return promotionSessionMapper.selectById(id);
    }

    @Override
    public List<SmsFlashPromotionSession> list() {
        return promotionSessionMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> result = new ArrayList<>();
        List<SmsFlashPromotionSession> list = promotionSessionMapper.selectList(new LambdaQueryWrapper<SmsFlashPromotionSession>().eq(SmsFlashPromotionSession::getStatus, 1));
        for (SmsFlashPromotionSession promotionSession : list) {
            SmsFlashPromotionSessionDetail detail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(promotionSession, detail);
            long count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }
}
