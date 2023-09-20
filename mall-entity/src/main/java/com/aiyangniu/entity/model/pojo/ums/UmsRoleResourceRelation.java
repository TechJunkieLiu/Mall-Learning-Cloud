package com.aiyangniu.entity.model.pojo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台角色资源关系实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "ums_role_resource_relation", description = "后台角色资源关系表实体")
public class UmsRoleResourceRelation implements Serializable {

    private static final long serialVersionUID = 7867514323521256941L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;
}