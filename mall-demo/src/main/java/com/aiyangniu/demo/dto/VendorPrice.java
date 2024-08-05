package com.aiyangniu.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VendorPrice {

    private String inquiryNo;
    private String source;
    private String materialCode;
    private String materialName;
    private String materialDesc;
    private String companyCode;
    private String companyName;
    private String purchasingGroupCode;
    private String purchasingGroupName;
    private String taxCode;
    private String vendorCode;
    private String vendorErpCode;
    private String vendorName;
    private String manuFactory;
    private String sourceGoods;
    private String currencyCode;
    private String currencyName;
    private String unitCode;
    private String unitName;
    private Integer priceBase;
    private String taxRateCode;
    private String taxRateValue;
    private BigDecimal pricingPrice;
    private BigDecimal factoryPrice;
    private BigDecimal freight;
    private BigDecimal otherExpenses;
    private BigDecimal pricingQty;
    private Integer planQty;
}
