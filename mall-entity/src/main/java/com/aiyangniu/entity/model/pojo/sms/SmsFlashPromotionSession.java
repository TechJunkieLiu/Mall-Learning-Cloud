package com.aiyangniu.entity.model.pojo.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 限时购场次实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "sms_flash_promotion_session", description = "限时购场次表实体")
public class SmsFlashPromotionSession implements Serializable {

    private static final long serialVersionUID = -2921156331448788979L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "场次名称")
    private String name;

    @ApiModelProperty(value = "每日开始时间")
    private Date startTime;

    @ApiModelProperty(value = "每日结束时间")
    private Date endTime;

    @ApiModelProperty(value = "启用状态：0->不启用；1->启用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}