package cms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 专题和商品关系实体
 *
 * @author lzq
 * @date 2023/09/08
 */
@Data
@ApiModel(value = "cms_subject_product_relation", description = "专题和商品关系表实体")
public class CmsSubjectProductRelation implements Serializable {

    private static final long serialVersionUID = 943612161588361346L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "专题ID")
    private Long subjectId;

    @ApiModelProperty(value = "商品ID")
    private Long productId;
}