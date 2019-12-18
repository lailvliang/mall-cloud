package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author dsy on 2019/11/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsInstallmentPlantDetail {
    @ApiModelProperty(value = "当前期数")
    private Integer curPeriod;
    @ApiModelProperty(value = "月供")
    private BigDecimal monthPay;
    @ApiModelProperty(value = "本金")
    private BigDecimal principal;
    @ApiModelProperty(value = "手续费")
    private BigDecimal serviceCharge;
}
