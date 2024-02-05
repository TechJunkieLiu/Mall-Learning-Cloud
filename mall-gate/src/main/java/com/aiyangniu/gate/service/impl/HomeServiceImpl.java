package com.aiyangniu.gate.service.impl;

import com.aiyangniu.common.utils.DateUtil;
import com.aiyangniu.entity.model.bo.FlashPromotionProduct;
import com.aiyangniu.entity.model.bo.HomeContentResult;
import com.aiyangniu.entity.model.bo.HomeFlashPromotion;
import com.aiyangniu.entity.model.pojo.cms.CmsSubject;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotion;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionSession;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeAdvertise;
import com.aiyangniu.gate.mapper.*;
import com.aiyangniu.gate.service.HomeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 首页内容管理实现类
 *
 * @author lzq
 * @date 2024/01/31
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HomeServiceImpl implements HomeService {

    private final SmsHomeAdvertiseMapper smsHomeAdvertiseMapper;
    private final HomeMapper homeMapper;
    private final SmsFlashPromotionMapper smsFlashPromotionMapper;
    private final SmsFlashPromotionSessionMapper smsFlashPromotionSessionMapper;
    private final PmsProductMapper pmsProductMapper;
    private final PmsProductCategoryMapper pmsProductCategoryMapper;
    private final CmsSubjectMapper cmsSubjectMapper;

    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        // 获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        // 获取推荐品牌
        result.setBrandList(homeMapper.getRecommendBrandList(0,6));
        // 获取秒杀信息
        result.setHomeFlashPromotion(getHomeFlashPromotion());
        // 获取新品推荐
        result.setNewProductList(homeMapper.getNewProductList(0,4));
        // 获取人气推荐
        result.setHotProductList(homeMapper.getHotProductList(0,4));
        // 获取推荐专题
        result.setSubjectList(homeMapper.getRecommendSubjectList(0,4));
        return result;
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO 暂时默认推荐所有商品
        PageHelper.startPage(pageNum, pageSize);
        return pmsProductMapper.selectList(new LambdaQueryWrapper<PmsProduct>().eq(PmsProduct::getDeleteStatus, 0).eq(PmsProduct::getPublishStatus, 1));
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        return pmsProductCategoryMapper.selectList(new LambdaQueryWrapper<PmsProductCategory>().eq(PmsProductCategory::getShowStatus, 1).eq(PmsProductCategory::getParentId, parentId).orderByDesc(PmsProductCategory::getSort));
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return cmsSubjectMapper.selectList(new LambdaQueryWrapper<CmsSubject>().eq(CmsSubject::getShowStatus, 1).eq(cateId != null, CmsSubject::getCategoryId, cateId));
    }

    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeMapper.getHotProductList(offset, pageSize);
    }

    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        int offset = pageSize * (pageNum - 1);
        return homeMapper.getNewProductList(offset, pageSize);
    }

    private HomeFlashPromotion getHomeFlashPromotion() {
        HomeFlashPromotion homeFlashPromotion = new HomeFlashPromotion();
        // 获取当前秒杀活动
        Date now = new Date();
        SmsFlashPromotion flashPromotion = getFlashPromotion(now);
        if (flashPromotion != null) {
            // 获取当前秒杀场次
            SmsFlashPromotionSession flashPromotionSession = getFlashPromotionSession(now);
            if (flashPromotionSession != null) {
                homeFlashPromotion.setStartTime(flashPromotionSession.getStartTime());
                homeFlashPromotion.setEndTime(flashPromotionSession.getEndTime());
                // 获取下一个秒杀场次
                SmsFlashPromotionSession nextSession = getNextFlashPromotionSession(homeFlashPromotion.getStartTime());
                if(nextSession != null){
                    homeFlashPromotion.setNextStartTime(nextSession.getStartTime());
                    homeFlashPromotion.setNextEndTime(nextSession.getEndTime());
                }
                // 获取秒杀商品
                List<FlashPromotionProduct> flashProductList = homeMapper.getFlashProductList(flashPromotion.getId(), flashPromotionSession.getId());
                homeFlashPromotion.setProductList(flashProductList);
            }
        }
        return homeFlashPromotion;
    }

    /**
     * 获取下一个场次信息
     */
    private SmsFlashPromotionSession getNextFlashPromotionSession(Date date) {
        List<SmsFlashPromotionSession> promotionSessionList = smsFlashPromotionSessionMapper.selectList(new LambdaQueryWrapper<SmsFlashPromotionSession>().gt(SmsFlashPromotionSession::getStartTime, date).orderByAsc(SmsFlashPromotionSession::getStartTime));
        if (!CollectionUtils.isEmpty(promotionSessionList)) {
            return promotionSessionList.get(0);
        }
        return null;
    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        return smsHomeAdvertiseMapper.selectList(new LambdaQueryWrapper<SmsHomeAdvertise>().eq(SmsHomeAdvertise::getType, 1).eq(SmsHomeAdvertise::getStatus, 1).orderByDesc(SmsHomeAdvertise::getSort));
    }

    /**
     * 根据时间获取秒杀活动
     */
    private SmsFlashPromotion getFlashPromotion(Date date) {
        Date currDate = DateUtil.getDate(date);
        List<SmsFlashPromotion> flashPromotionList = smsFlashPromotionMapper.selectList(new LambdaQueryWrapper<SmsFlashPromotion>()
                .eq(SmsFlashPromotion::getStatus, 1)
                .le(SmsFlashPromotion::getStartDate, currDate)
                .ge(SmsFlashPromotion::getEndDate, currDate)
        );
        if (!CollectionUtils.isEmpty(flashPromotionList)) {
            return flashPromotionList.get(0);
        }
        return null;
    }

    /**
     * 根据时间获取秒杀场次
     */
    private SmsFlashPromotionSession getFlashPromotionSession(Date date) {
        Date currTime = DateUtil.getTime(date);
        List<SmsFlashPromotionSession> promotionSessionList = smsFlashPromotionSessionMapper.selectList(new LambdaQueryWrapper<SmsFlashPromotionSession>().le(SmsFlashPromotionSession::getStartTime, currTime).ge(SmsFlashPromotionSession::getEndTime, currTime));
        if (!CollectionUtils.isEmpty(promotionSessionList)) {
            return promotionSessionList.get(0);
        }
        return null;
    }
}
