package com.macro.mall.domain;

import com.macro.mall.dto.PortalHomeProductDetail;
import lombok.Data;

import java.util.List;

/**
 * @author madaijun
 * @version 1.0
 * @date 2019\11\15 0015 14:47
 */
@Data
public class MallHomeProduct {
    /**
     * 一级分类名称
     */
    private String categoryName;
    /**
     * 一级分类id
     */
    private Integer categoryId;
    /**
     * 二级分类商品列表
     */
    private List<SubGroupProduct> products;

    @Data
    public static class SubGroupProduct {
        /**
         * 二级分类名称
         */
        private String categoryName;
        /**
         * 二级分类id
         */
        private Integer categoryId;
        /**
         * 二级分类商品列表
         */
        private List<PortalHomeProductDetail> products;
    }

}
