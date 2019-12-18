package com.macro.mall.domain;

import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductFullReduction;
import com.macro.mall.model.PmsProductLadder;
import com.macro.mall.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 首页商品列表展示详情model
 * 商品的促销信息，包括sku、打折优惠、满减优惠
 * @author madaijun
 * @version 1.0
 * @date 2019\11\14 0014 10:43
 */
public class PromotionProduct extends PmsProduct {
    //商品库存信息
    private List<PmsSkuStock> skuStockList;
    //商品打折信息
    private List<PmsProductLadder> productLadderList;
    //商品满减信息
    private List<PmsProductFullReduction> productFullReductionList;
    //二级分类名称
    private String chCategoryName;
    //一级分类名称
    private String paCategoryName;
    //二级分类id
    private int chCategoryId;
    //一级分类id
    private int paCategoryId;
    @ApiModelProperty(value = "分期")
    private String payPeriods;

    public List<PmsSkuStock> getSkuStockList() {
        return skuStockList;
    }

    public void setSkuStockList(List<PmsSkuStock> skuStockList) {
        this.skuStockList = skuStockList;
    }

    public List<PmsProductLadder> getProductLadderList() {
        return productLadderList;
    }

    public void setProductLadderList(List<PmsProductLadder> productLadderList) {
        this.productLadderList = productLadderList;
    }

    public List<PmsProductFullReduction> getProductFullReductionList() {
        return productFullReductionList;
    }

    public void setProductFullReductionList(List<PmsProductFullReduction> productFullReductionList) {
        this.productFullReductionList = productFullReductionList;
    }

    public String getChCategoryName() {
        return chCategoryName;
    }

    public void setChCategoryName(String chCategoryName) {
        this.chCategoryName = chCategoryName;
    }

    public String getPaCategoryName() {
        return paCategoryName;
    }

    public void setPaCategoryName(String paCategoryName) {
        this.paCategoryName = paCategoryName;
    }

    public int getChCategoryId() {
        return chCategoryId;
    }

    public void setChCategoryId(int chCategoryId) {
        this.chCategoryId = chCategoryId;
    }

    public int getPaCategoryId() {
        return paCategoryId;
    }

    public void setPaCategoryId(int paCategoryId) {
        this.paCategoryId = paCategoryId;
    }
}
