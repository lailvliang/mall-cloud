package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 获取分期计划的参数
 * @author dsy on 2019/11/15
 */
@Data
public class PmsInstallmentPlantParam {
    /**
     * 商品id
     */
    @ApiModelProperty("商品ID，必填")
    @NotNull(message = "productId不能为空或空字符等")
    private Long productId;

    @ApiModelProperty("skuCode")
    private String skuCode;

    @ApiModelProperty("期数periods，必填")
    @NotNull(message = "periods不能为空或空字符等")
    @Min(0)
    private Integer periods;

    @ApiModelProperty("商品价格，单位/元")
    private String productPrice;


}
