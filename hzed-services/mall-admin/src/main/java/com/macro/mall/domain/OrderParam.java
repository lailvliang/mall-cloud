package com.macro.mall.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 生成订单时传入的参数
 * Created by macro on 2018/8/30.
 */
@Data
public class OrderParam {
    @ApiModelProperty("优惠券id")
    private Long couponId;
    @ApiModelProperty("使用的积分数")
    private Integer useIntegration;
    @ApiModelProperty("支付方式 默认为分期:4")
    private Integer payType = 4;
    @ApiModelProperty("分期数")
    @NotNull(message = "分期数不能为空")
    private int payPeriod;
    @ApiModelProperty("商品id")
    @NotNull(message = "商品id不能为空")
    private Long productId;
    @ApiModelProperty("购买商品数量 默认值:1")
    private int productQuantity = 1;
    @ApiModelProperty("skuId")
    @NotNull(message = "skuId不能为空")
    private Long skuId;
}
