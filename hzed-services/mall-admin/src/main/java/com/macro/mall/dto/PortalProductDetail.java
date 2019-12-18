package com.macro.mall.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author yyq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortalProductDetail  {

    @ApiModelProperty(value = "商品id")
    private Long id;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "新品状态:0->不是新品；1->新品")
    private Integer newStatus;
    @ApiModelProperty(value = "副标题")
    private String subTitle;
    @ApiModelProperty(value = "库存")
    private Integer stock;
    @ApiModelProperty(value = "画册图片，连产品图片限制为5张，以逗号分割")
    private String albumPics;
    @ApiModelProperty(value = "默认最低价格")
    private BigDecimal price;
    @ApiModelProperty(value = "商品规格类型")
    private List<AttributeType> types;
    @ApiModelProperty(value = "商品sku")
    private List<SkuProduct> skuProductsList;
    @ApiModelProperty(value = "移动端图文详情")
    private String detailMobileHtml;
    @ApiModelProperty(value = "分期")
    private String payPeriods;

    @JsonIgnore
    /**数据库查询出来 在转换成Types, 数据库中存的是形容"红色,白色,黑色",是字符串,需转化成数组(集合)对象*/
    private List<ProductAttribute> productAttributeList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AttributeType {
        @ApiModelProperty(value = "商品规格名称")
        private String name;
        @ApiModelProperty(value = "商品规格类型值")
        private List<String> value;
        @ApiModelProperty(value = "排序")
        private int sort;
    }

    /**
     *将productAttributeList 转换成 types 返回前端,相当于一个拷贝
     */
    public PortalProductDetail transformTypes(){
        if(!CollectionUtils.isEmpty(productAttributeList)){
            types = Lists.newArrayListWithCapacity(productAttributeList.size());
            for(ProductAttribute productAttribute : productAttributeList){
                AttributeType attributeType = new AttributeType();
                BeanUtils.copyProperties(productAttribute,attributeType);
                attributeType.setValue(getListValue(productAttribute));
                types.add(attributeType);
            }
        }
        return this;
    }

    private List<String> getListValue(ProductAttribute productAttribute) {
        return productAttribute.getValue()==null ? null : Lists.newArrayList(productAttribute.getValue().split(","));
    }

    public PortalProductDetail transformSkuProducts() throws Exception{
        if(!CollectionUtils.isEmpty(skuProductsList)) {
            skuProductsList.sort(Comparator.comparing(SkuProduct::getPrice));
            for (SkuProduct skuProduct : skuProductsList) {
                //将属性字符串转换成List对象
                skuProduct.transformProsList();
                //计算一期的月供(本金+手续费)
                skuProduct.calculatePerPeriodPrice(payPeriods);
            }
        }
        return this;
    }
}
