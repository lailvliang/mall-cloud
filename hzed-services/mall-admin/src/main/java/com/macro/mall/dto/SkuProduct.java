package com.macro.mall.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.macro.mall.common.MathUtil;
import com.macro.mall.common.SpringBeanUtils;
import com.macro.mall.service.CalculatePeriodAmountService;
import com.macro.mall.service.impl.CalculatePeriodAmountServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuProduct implements Serializable {
    private  static ObjectMapper mapper = new ObjectMapper();


    private Long id;
    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "sku价格")
    private BigDecimal price;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "展示图片")
    private String pic;

    /**展示属性值字符串 数据库中形容“[{"name":"颜色","value":"红色"},{"name":"尺码","value":"30"}]”*/
    @JsonIgnore
    private String dynamicPros;

    @ApiModelProperty(value = "商品属性值")
    private List<DynamicPros> dynamicProsList;

    @ApiModelProperty(value = "商品分期")
    private List<Periods> periods;

    @Data
    @NoArgsConstructor
    static class DynamicPros{
        @ApiModelProperty(value = "商品具体规格名称")
        private String name;
        @ApiModelProperty(value = "商品具体规格类型值")
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Periods{
        @ApiModelProperty(value = "商品分期类型")
        private String times;
        @ApiModelProperty(value = "商品分期首期价格")
        private BigDecimal price;
        @ApiModelProperty(value = "单件实付总额既还款金额(金额+手续费*期数)")
        private BigDecimal repayTotalAmount;
    }

    /**
     * 将从数据库取出的属性类型“[{"name":"颜色","value":"红色"},{"name":"尺码","value":"30"}]”
     * 转换成list对象给前端
     * @return
     * @throws Exception
     */
    public SkuProduct transformProsList() throws Exception{
        if(!Strings.isNullOrEmpty(dynamicPros)){
            dynamicProsList = mapper.readValue(dynamicPros, new TypeReference<List<DynamicPros>>(){});
        }
        return this;
    }

    /**
     * 只计算一期的月供（本金加手续费）
     * @param payPeriods
     * @return
     */
    public SkuProduct calculatePerPeriodPrice(String payPeriods){
        if(!Strings.isNullOrEmpty(payPeriods) && MathUtil.gt(price,BigDecimal.ZERO)){
            CalculatePeriodAmountService<BigDecimal,String,BigDecimal> calculatePeriodAmount = SpringBeanUtils.getBean("calculatePeriodAmountServiceImpl", CalculatePeriodAmountService.class);
            List<String> payPeriodList = Lists.newArrayList(payPeriods.split(","));

            periods = Lists.newArrayListWithCapacity(payPeriodList.size());
            for (String payPeriod : payPeriodList){
                //分期本金
                BigDecimal principal = calculatePeriodAmount.calculatePerRepayAmount(price,payPeriod);
                //手续费
                BigDecimal charge = calculatePeriodAmount.calculatePerCharge(price, CalculatePeriodAmountServiceImpl.getScale());
                //实付总额既还款金额(商品金额+手续费*期数)==单件的还款金额
                BigDecimal repayTotalAmount =MathUtil.add(price,MathUtil.mul(charge,new BigDecimal(payPeriod)));
                periods.add(new Periods(payPeriod,MathUtil.add(principal,charge),repayTotalAmount));
            }
        }
        return  this;
    }

}
