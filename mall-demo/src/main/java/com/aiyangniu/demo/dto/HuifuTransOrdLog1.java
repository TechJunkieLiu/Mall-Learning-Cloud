package com.aiyangniu.demo.dto;

import com.opencsv.bean.CsvBindByPosition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 汇付交易订单记录
 *
 * @author lzq
 * @date 2024/02/28
 */
@Data
@ApiModel(value = "huifu_trans_ord_log", description = "汇付交易订单记录")
public class HuifuTransOrdLog1 implements Serializable {

    @ApiModelProperty("渠道商号")
    @CsvBindByPosition(position = 0)
    private String sysId;

    @ApiModelProperty("商户号")
    @CsvBindByPosition(position = 1)
    private String huifuId;

    @ApiModelProperty("商户名称")
    @CsvBindByPosition(position = 2)
    private String regName;
}
