package ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录日志记录实体
 *
 * @author lzq
 * @date 2023/09/08
 */
@Data
@ApiModel(value = "ums_admin_login_log", description = "用户登录日志记录表实体")
public class UmsAdminLoginLog implements Serializable {

    private static final long serialVersionUID = -9017282320239273109L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long adminId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "浏览器登录类型")
    private String userAgent;
}
