package com.aiyangniu.entity.model.pojo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关系实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "ums_admin_role_relation", description = "用户角色关系表实体")
public class UmsAdminRoleRelation implements Serializable {

    private static final long serialVersionUID = -4340740752499149042L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long adminId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;
}