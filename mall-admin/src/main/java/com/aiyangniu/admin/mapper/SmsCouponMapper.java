package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.dto.SmsCouponParamDTO;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券Mapper
 *
 * @author lzq
 * @date 2024/02/28
 */
public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {

    /**
     * 获取优惠券详情包括绑定关系
     *
     * @param id 优惠券ID
     * @return 优惠券详情
     */
    SmsCouponParamDTO getItem(@Param("id") Long id);
}
