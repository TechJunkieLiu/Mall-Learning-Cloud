package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.pms.PmsProductFullReduction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品满减Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface PmsProductFullReductionMapper extends BaseMapper<PmsProductFullReduction> {

    /**
     * 批量创建
     *
     * @param productFullReductionList 产品满减列表
     * @return 创建个数
     */
    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
