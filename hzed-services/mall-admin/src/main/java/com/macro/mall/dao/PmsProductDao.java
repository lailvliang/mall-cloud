package com.macro.mall.dao;

import com.macro.mall.domain.CartProduct;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.dto.PortalProductDetail;
import com.macro.mall.dto.PortalHomeProductDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 商品自定义Dao
 * Created by macro on 2018/4/26.
 */
@Repository
public interface PmsProductDao {
    /**
     * 获取下单商品详情
     * @param id
     * @return
     */
    CartProduct getCartProduct(@Param("id") Long id);
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);

    /**
     * 获取上架促销热门推荐商品top 3
     * @return
     */
    List<PortalHomeProductDetail> getHotProductListTopThree();

    /**
     * 获取首页促销商品分类展示列表
     * 展示分类：获取后台“商品分类”中“排序”前3个一级分类,再获取各个一级分类下的“排序”前3的二级分类，在获取各个二级分类下的6个随机商品（总商品数量：3*3*6=54个）
     * 分类标题展示：二级标题。每个二级标题展示顺序随机，如图“数码类”和“美妆类”。
     * 商品展示：获取各个二级分类下的6个随机商品个，如图6个商品。
     * @return
     */
    List<PortalHomeProductDetail> getCategoryProductList();

    PortalProductDetail getPortalProductDetail(@Param("id") Long id);
}
