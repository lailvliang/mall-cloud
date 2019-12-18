package com.macro.mall.dao;

import com.macro.mall.model.PmsSkuStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 自定义商品sku库存Dao
 * Created by macro on 2018/4/26.
 */
@Repository
public interface PmsSkuStockDao {
    /**
     * 批量插入操作
     */
    int insertList(@Param("list") List<PmsSkuStock> skuStockList);

    /**
     * 批量插入或替换操作
     */
    int replaceList(@Param("list") List<PmsSkuStock> skuStockList);

    List<PmsSkuStock> find(@Param("skuCode") String skuCode, @Param("productId") Long productId);

    /**
     * 根据主键id 查找sku
     * @param skuId
     * @return
     */
    PmsSkuStock findById(@Param("skuId") Long skuId);
}
