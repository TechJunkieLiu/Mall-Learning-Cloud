package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.bo.OmsOrderReturnApplyResult;
import com.aiyangniu.entity.model.dto.OmsReturnApplyQueryParamDTO;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单退货申请管理Mapper
 *
 * @author lzq
 * @date 2024/01/18
 */
public interface OmsOrderReturnApplyMapper extends BaseMapper<OmsOrderReturnApply> {

    /**
     * 查询申请列表
     *
     * @param queryParam 查询参数
     * @return 申请列表
     */
    List<OmsOrderReturnApply> getList(@Param("queryParam") OmsReturnApplyQueryParamDTO queryParam);

    /**
     * 获取申请详情
     *
     * @param id 退货申请单号
     * @return 货申请详情
     */
    OmsOrderReturnApplyResult getDetail(@Param("id") Long id);
}
