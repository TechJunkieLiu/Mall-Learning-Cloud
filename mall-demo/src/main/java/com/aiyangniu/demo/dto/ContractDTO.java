//package com.aiyangniu.demo.dto;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * 合同参数
// *
// * @author lzq
// * @date 2024/03/12
// */
//@Data
//@ApiModel(value = "ContractDTO")
//public class ContractDTO {
//
//    @NotBlank(message = "分标人不能为空")
//    @ApiModelProperty(value = "分标人", required = true)
//    private String scorerUserCode;
//
//    @NotNull(message = "出厂单价不能为空")
//    @ApiModelProperty(value = "出厂单价", required = true)
//    private BigDecimal factoryPrice;
//
//    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
//    @ApiModelProperty("合同开始时间")
//    private Date contractStartDate;
//}
