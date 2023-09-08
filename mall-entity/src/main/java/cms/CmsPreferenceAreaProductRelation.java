package cms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 优选专区和商品的关系实体
 *
 * @author lzq
 * @date 2023/09/08
 */
@Data
@ApiModel(value = "cms_preference_area_product_relation", description = "优选专区和商品的关系表实体")
public class CmsPreferenceAreaProductRelation implements Serializable {

    private static final long serialVersionUID = -8156099590373422715L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "优选专区ID")
    private Long preferenceAreaId;

    @ApiModelProperty(value = "商品ID")
    private Long productId;
}