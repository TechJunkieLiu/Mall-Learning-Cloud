package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

/**
 * 商品品牌Mapper
 *
 * @author lzq
 * @date 2024/01/26
 */
public interface PmsBrandMapper extends BaseMapper<PmsBrand> {

    /**
     * 根据品牌编号获取品牌
     *
     * @param id 品牌编号
     * @return 商品品牌
     */
    @Cacheable(cacheNames = "springCacheOld", keyGenerator = "interfaceKeyGenerator")
    PmsBrand getBrandById(@Param("id") Long id);
}
