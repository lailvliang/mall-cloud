package com.macro.mall.domain;

import com.macro.mall.dto.PortalHomeProductDetail;
import lombok.Data;

import java.util.List;

/**
 * 商城首页详情展示详情
 * @author madaijun
 * @version 1.0
 * @date 2019\11\14 0014 11:18
 */
@Data
public class MallHomeDetail {
    private List<PortalHomeProductDetail> hotProducts;
    /**
     * 商城首页展示的商品列表数据
     */
    private List<MallHomeProduct> categoryProductMap;
}
