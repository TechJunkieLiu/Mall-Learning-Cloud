package pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品属性分类实体
 *
 * @author lzq
 * @date 2023/09/08
 */
@Data
@ApiModel(value = "pms_product_attribute_category", description = "商品属性分类表实体")
public class PmsProductAttributeCategory implements Serializable {

    private static final long serialVersionUID = 6790060434235666492L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品属性分类名称")
    private String name;

    @ApiModelProperty(value = "属性数量")
    private Integer attributeCount;

    @ApiModelProperty(value = "参数数量")
    private Integer paramCount;
}