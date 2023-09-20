package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionSession;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 包含商品数量的场次信息
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class SmsFlashPromotionSessionDetail extends SmsFlashPromotionSession {

    private static final long serialVersionUID = -1729640148452805868L;

    @ApiModelProperty("商品数量")
    private Long productCount;
}
