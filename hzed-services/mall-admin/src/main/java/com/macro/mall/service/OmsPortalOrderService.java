package com.macro.mall.service;

import com.macro.mall.common.CommonResult;
import com.macro.mall.domain.OrderParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 前台订单管理Service
 * Created by macro on 2018/8/30.
 */
public interface OmsPortalOrderService {

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult generateOrder(OrderParam orderParam) throws Exception;

    /**
     * 支付成功后的回调
     */
    @Transactional
    CommonResult paySuccess(Long orderId);

}
