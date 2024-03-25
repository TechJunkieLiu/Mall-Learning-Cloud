package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.bo.SmsCouponHistoryDetail;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券使用、领取历史Mapper
 *
 * @author lzq
 * @date 2024/03/22
 */
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistory> {

    /**
     * 获取优惠券历史详情
     *
     * @param memberId 会员ID
     * @return 优惠券历史详情
     */
    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);
}
