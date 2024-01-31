package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.pms.PmsProductVerifyRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品审核日志管理Mapper
 *
 * @author lzq
 * @date 2024/01/26
 */
public interface PmsProductVerifyRecordMapper {

    /**
     * 批量创建
     *
     * @param list 商品审核日志列表
     * @return 创建个数
     */
    int insertList(@Param("list") List<PmsProductVerifyRecord> list);
}
