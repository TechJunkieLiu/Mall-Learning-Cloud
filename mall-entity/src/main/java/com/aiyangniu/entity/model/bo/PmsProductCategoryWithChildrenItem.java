package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 包含子级分类的商品分类
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
public class PmsProductCategoryWithChildrenItem extends PmsProductCategory {

    private static final long serialVersionUID = -516796889566046799L;

    @ApiModelProperty("子级分类")
    private List<PmsProductCategory> children;
}
