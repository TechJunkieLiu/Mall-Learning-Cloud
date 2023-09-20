package com.aiyangniu.entity.model.pojo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台角色菜单关系实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "ums_role_menu_relation", description = "后台角色菜单关系表实体")
public class UmsRoleMenuRelation implements Serializable {

    private static final long serialVersionUID = 1322210467385906554L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;
}