package com.aiyangniu.demo.dto;

import com.opencsv.bean.CsvBindByName;
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
public class HuifuTransOrdLog2 implements Serializable {

    @ApiModelProperty("渠道商号")
    @CsvBindByName(column = "=\"渠道商号\"")
    private String sysId;

    @ApiModelProperty("商户号")
    @CsvBindByName(column = "=\"商户号\"")
    private String huifuId;

    @ApiModelProperty("商户名称")
    @CsvBindByName(column = "=\"商户名称\"")
    private String regName;
}
