package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.ums.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {

    private static final long serialVersionUID = -230959500010970929L;

    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode> children;

    @Override
    public String toString() {
        return "UmsMenuNodeDTO{" +
                "children=" + children +
                '}';
    }
}
